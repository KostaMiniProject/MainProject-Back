package kosta.main.exchangeposts.service;

import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangeposts.dto.*;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kosta.main.global.error.exception.CommonErrorCode.*;

@Service
@AllArgsConstructor
public class ExchangePostsService {

  private final ExchangePostsRepository exchangePostRepository;
  private final ItemsRepository itemsRepository;
  private final BidRepository bidRepository;
  
  // 공통메서드 : 게시글 삭제시 item들의 상태를 변경해주는 기능
  private void updateItemsBidingStatus(List<Item> items, Item.IsBiding status, Bid bid) {
    for (Item item : items) {
      item.updateIsBiding(status);
      item.updateBid(bid); // Bid 참조 업데이트, bid가 null일 경우 참조 제거
      itemsRepository.save(item);
    }
  }

  @Transactional
  public ResponseDto createExchangePost(User user, ExchangePostDTO exchangePostDTO) {
    // 기존 코드: 사용자 및 아이템 조회
    Item item = itemsRepository.findById(exchangePostDTO.getItemId())
            .orElseThrow(() -> new BusinessException(ITEM_NOT_FOUND));
    System.out.println(item.getItemId());

    // 아이템 소유주 확인
    if (!item.getUser().getUserId().equals(user.getUserId())) {
      throw new BusinessException(NOT_ITEM_OWNER);
    }
    if (item.getIsBiding() == Item.IsBiding.BIDING) {
      throw new BusinessException(ALREADY_BIDDING_ITEM);
    }

    // 교환 게시글에 사용된 Item의 상태는 다른 거래에 사용하지 못하도록 BIDING으로 변경되어야한다.
    item.updateIsBiding(Item.IsBiding.BIDING);
    itemsRepository.save(item);

    // ExchangePost 엔티티 생성
    ExchangePost exchangePost = ExchangePost.builder()
            .user(user)
            .item(item)
            .title(exchangePostDTO.getTitle())
            .preferItems(exchangePostDTO.getPreferItems())
            .address(exchangePostDTO.getAddress())
            .content(exchangePostDTO.getContent())
            .build();
    ExchangePost savedExchangePost = exchangePostRepository.save(exchangePost);

    //23.12.02 우선은 생성된 교환게시글의 ID만 날려주고 추후 응답 컨벤션이 제대로 지정되면 변경예정
    return ResponseDto.of(savedExchangePost.getExchangePostId());
  }



  // 전체 게시글 불러오기 (23.12.04 : 최신순으로 제공, 페이지 네이션으로 10개씩 제공)
  @Transactional(readOnly = true)
  public Page<ExchangePostListDTO> findAllExchangePosts(Pageable pageable) {
    Page<ExchangePost> all = exchangePostRepository.findAll(pageable);
    return all
        .map(post -> {
          // 아이템 대표 이미지 URL을 가져오는 로직 (첫 번째 이미지를 대표 이미지로 사용)
          String imgUrl = !post.getItem().getImages().isEmpty() ? post.getItem().getImages().get(0) : null;

          // 해당 교환 게시글에 입찰된 Bid의 갯수를 세는 로직 + BidStatus가 DELETED인 것은 세지 않도록 하는 로직
          Integer bidCount = bidRepository.countByExchangePostAndStatusNotDeleted(post);

          return ExchangePostListDTO.builder()
              .exchangePostId(post.getExchangePostId())
              .title(post.getTitle())
              .preferItem(post.getPreferItems())
              .address(post.getAddress())
              .exchangePostStatus(post.getExchangePostStatus().toString())
              .createdAt(post.getCreatedAt())
              .imgUrl(imgUrl)
              .bidCount(bidCount)
              .build();
        });
  }



  @Transactional(readOnly = true)
  public ExchangePostDetailDTO findExchangePostById(Integer exchangePostId, User currentUser) {
    ExchangePost post = exchangePostRepository.findById(exchangePostId)
        .orElseThrow(() -> new BusinessException(EXCHANGE_POST_NOT_FOUND));

    // 교환 게시글 작성자와 현재 로그인한 사용자가 같은지 확인 (로그인하지 않은 경우 고려)
    boolean isOwner = currentUser != null && post.getUser().getUserId().equals(currentUser.getUserId());

    // 사용자 프로필 정보 생성
    ExchangePostDetailDTO.UserProfile userProfile = ExchangePostDetailDTO.UserProfile.builder()
        .userId(post.getUser().getUserId())
        .name(post.getUser().getName())
        .address(post.getUser().getAddress())
        .imageUrl(post.getUser().getProfileImage())
        .rating((post.getUser().getRating()))
        .build();

    // 아이템 상세 정보 생성
    ExchangePostDetailDTO.ItemDetails itemDetails = ExchangePostDetailDTO.ItemDetails.builder()
            .itemId(post.getItem().getItemId())
        .title(post.getItem().getTitle())
        .description(post.getItem().getDescription())
        .imageUrls(post.getItem().getImages())
        .build();

    // 입찰 목록 생성
    List<ExchangePostDetailDTO.BidDetails> bidDetailsList = post.getBids().stream()
        .map(bid -> ExchangePostDetailDTO.BidDetails.builder()
            .bidId(bid.getBidId())
            .name(bid.getUser().getName())
            .imageUrl(bid.getUser().getProfileImage())
            .items(convertItemListToString(bid.getItems())) // 예시: 아이템 목록을 문자열로 변환하는 메서드
            .build())
        .collect(Collectors.toList());

    // ExchangePostDetailDTO 구성
    return ExchangePostDetailDTO.builder()
        .postOwner(isOwner)
        .title(post.getTitle())
        .preferItems(post.getPreferItems())
        .address(post.getAddress())
        .content(post.getContent())
        .profile(userProfile)
        .item(itemDetails)
        .bidList(bidDetailsList)
        .build();
  }

  private String convertItemListToString(List<Item> items) {
    // 아이템 리스트를 문자열로 변환하는 로직 구현
    return items.stream()
        .map(Item::getTitle)
        .collect(Collectors.joining(", "));
  }

  @Transactional
  public ExchangePostUpdateResponseDTO updateExchangePost(User user, Integer exchangePostId, ExchangePostDTO updatedExchangePostDTO) {
    ExchangePost existingExchangePost = exchangePostRepository.findById(exchangePostId)
        .orElseThrow(() -> new BusinessException(EXCHANGE_POST_NOT_FOUND));

    // 요청 사용자와 게시글 작성자가 동일한지 확인
    if (!existingExchangePost.getUser().getUserId().equals(user.getUserId())) {
      throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
    }

    // Item 엔티티 조회 및 상태 확인 및 변경
    Item newItem = null; // 생성되지 않을수 있기 떄문에 우선 선언
    if (updatedExchangePostDTO.getItemId() != null) {
      newItem = itemsRepository.findById(updatedExchangePostDTO.getItemId())
          .orElseThrow(() -> new BusinessException(ITEM_NOT_FOUND));

      // 아이템 소유주 확인
      if (!newItem.getUser().getUserId().equals(user.getUserId())) {
        throw new BusinessException(NOT_ITEM_OWNER);
      }

      // 새 아이템의 상태를 BIDING으로 변경
      newItem.updateIsBiding(Item.IsBiding.BIDING);
      itemsRepository.save(newItem);
    }

    // 기존 아이템의 상태를 NOT_BIDING으로 변경 (새 아이템이 있고, 기존 아이템과 다른 경우)
    Item existingItem = existingExchangePost.getItem();
    if (newItem != null && !existingItem.equals(newItem)) {
      existingItem.updateIsBiding(Item.IsBiding.NOT_BIDING);
      itemsRepository.save(existingItem);
    }

    // ExchangePost 엔티티 업데이트
    existingExchangePost.updateWithBuilder(user, newItem != null ? newItem : existingItem, updatedExchangePostDTO);
    return ExchangePostUpdateResponseDTO.from(existingExchangePost);
  }

  @Transactional
  public void deleteExchangePost(Integer exchangePostId, User user) {
    ExchangePost existingExchangePost = exchangePostRepository.findById(exchangePostId)
        .orElseThrow(() -> new BusinessException(EXCHANGE_POST_NOT_FOUND));

    // 게시글 작성자와 삭제 요청자가 동일한지 확인
    if (!existingExchangePost.getUser().getUserId().equals(user.getUserId())) {
      throw new BusinessException(NOT_EXCHANGE_POST_OWNER);
    }

    // 관련된 모든 입찰을 찾아서 처리 (Soft Delete로 상태 변경)
    List<Bid> bids = bidRepository.findByExchangePost(existingExchangePost);
    for (Bid bid : bids) {
      updateItemsBidingStatus(bid.getItems(), Item.IsBiding.NOT_BIDING, null); // 아이템 상태 변경 및 bid 참조 제거
      bidRepository.delete(bid); // Soft Delete 실행
    }

    // 게시글 작성자의 아이템 상태를 NOT_BIDING으로 변경
    Item associatedItem = existingExchangePost.getItem();
    associatedItem.updateIsBiding(Item.IsBiding.NOT_BIDING);
    itemsRepository.save(associatedItem);

    // 게시글을 Soft Delete로 처리
    exchangePostRepository.delete(existingExchangePost);
  }

}
