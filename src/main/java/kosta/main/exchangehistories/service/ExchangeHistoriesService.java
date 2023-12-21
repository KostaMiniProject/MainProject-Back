package kosta.main.exchangehistories.service;

import jakarta.persistence.EntityNotFoundException;
import kosta.main.bids.entity.Bid;
import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangehistories.dto.ExchangeHistoryCreateDTO;
import kosta.main.exchangehistories.dto.ExchangeHistoriesResponseDTO;
import kosta.main.exchangehistories.dto.ExchangeHistoryCreateResponseDTO;
import kosta.main.exchangehistories.dto.ItemHistoryDTO;
import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangehistories.entity.ItemInfo;
import kosta.main.exchangehistories.repository.ExchangeHistoriesRepository;
import kosta.main.exchangehistories.repository.ItemInfoRepository;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.items.entity.Item;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExchangeHistoriesService {
  private final ExchangeHistoriesRepository exchangeHistoriesRepository;
  private final ExchangePostsRepository exchangePostsRepository;
  private final ItemInfoRepository itemInfoRepository;
  private final BidRepository bidRepository;


  // 공통 메서드: 특정 ID를 가진 엔티티를 찾고, 없으면 예외를 발생시키는 메서드
  private <T> T findEntityById(JpaRepository<T, Integer> repository, Integer id, String errorMessage) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException(errorMessage));
  }

  // 교환 내역 생성 = 거래완료 후의 로직
  @Transactional
  public ExchangeHistoryCreateResponseDTO createExchangeHistory(User user, Integer selectedBidId,Integer exchangePostId) {
    // 교환 게시글 정보 가져오기
    ExchangePost exchangePost = findEntityById(exchangePostsRepository, exchangePostId, "ExchangePost not found");

    // 선택된 입찰 정보 가져오기
    Bid selectedBid =
            exchangePost.getBids().stream().filter(bid -> Objects.equals(bid.getBidId(), selectedBidId)).findFirst().orElseThrow(() -> new BusinessException(CommonErrorCode.SELECTED_BID_NOT_FOUND));

    // 게시글 작성자 정보
    User postUser = exchangePost.getUser();
    // 입찰자 정보
    User bidUser = selectedBid.getUser();
    // 게시글의 아이템 정보
    Item postItem = exchangePost.getItem();
    ItemInfo itemInfo = ItemInfo.from(postItem);



    // 아이템 정보를 ItemHistoryDTO 리스트로 변환
    List<ItemHistoryDTO> exchangedItems = createItemHistoryList(selectedBid, postItem);

    // ExchangeHistory 객체 생성 및 저장
    ExchangeHistory exchangeHistory = ExchangeHistory.builder()
        .exchangeInitiator(postUser)
        .exchangePartner(bidUser)
        .exchangePost(exchangePost)
        .itemInfo(itemInfo)
        .exchangedItems(exchangedItems) // 추가된 필드
        .build();
    itemInfoRepository.save(itemInfo);
    ExchangeHistory savedExchangeHistory = exchangeHistoriesRepository.save(exchangeHistory);

    return new ExchangeHistoryCreateResponseDTO(savedExchangeHistory.getExchangeHistoryId());
  }

  // 아이템 정보를 ItemHistoryDTO 리스트로 변환하는 로직
  private List<ItemHistoryDTO> createItemHistoryList(Bid selectedBid, Item postItem) {
    List<ItemHistoryDTO> exchangedItems = new ArrayList<>();
    // 게시글 아이템 정보 추가
    exchangedItems.add(new ItemHistoryDTO(
        postItem.getTitle(),
        postItem.getDescription(),
        postItem.getImages().get(0)
    ));
    // 입찰 아이템 정보 추가
    for (Item item : selectedBid.getItems()) {
      exchangedItems.add(new ItemHistoryDTO(
          item.getTitle(),
          item.getDescription(),
          item.getImages().get(0)
      ));
    }
    return exchangedItems;
  }

  @Transactional(readOnly = true)
  public Page<ExchangeHistoriesResponseDTO> getExchangeHistories(User user, Pageable pageable) {
    Page<ExchangeHistory> histories = exchangeHistoriesRepository.findByExchangeInitiator_UserIdOrExchangePartner_UserId(user.getUserId(), user.getUserId(), pageable);

    List<ExchangeHistoriesResponseDTO> exchangeHistoriesResponseDTOS = makeExchangeHistoriesResponseDTO(histories);
    PageRequest pageRequest = PageRequest.of(histories.getNumber(), histories.getSize());
    int start = (int) pageRequest.getOffset();
    int end = Math.min((start + pageRequest.getPageSize()), exchangeHistoriesResponseDTOS.size());
    return new PageImpl<>(exchangeHistoriesResponseDTOS.subList(start, end), pageRequest, exchangeHistoriesResponseDTOS.size());
  }

  private static List<ExchangeHistoriesResponseDTO> makeExchangeHistoriesResponseDTO(Page<ExchangeHistory> histories) {
    return histories.stream().map(history -> {
      ExchangePost exchangePost = history.getExchangePost();
      Bid selectedBid = history.getExchangePost().getBids().stream()
              .filter(bid -> bid.getStatus() == Bid.BidStatus.SELECTED)
              .findFirst()
              .orElseThrow(() -> new EntityNotFoundException("Selected bid not found"));
      List<ItemHistoryDTO> exchangeHistory = history.getExchangedItems();

      List<ExchangeHistoriesResponseDTO.ItemDetailsDTO> myItems = Collections.singletonList(
              new ExchangeHistoriesResponseDTO.ItemDetailsDTO(
                      exchangePost.getItem().getItemId(),
                      exchangePost.getItem().getTitle(),
                      exchangePost.getItem().getDescription(),
                      exchangePost.getItem().getImages().isEmpty() ? null : exchangePost.getItem().getImages().get(0)
              )
      );

      List<ExchangeHistoriesResponseDTO.ItemHistoryDTO> otherUserItems = exchangeHistory.stream()
              .map(item -> new ExchangeHistoriesResponseDTO.ItemHistoryDTO(
                      item.getTitle(),
                      item.getDescription(),
                      item.getImageUrl().isEmpty() ? null : Collections.singletonList(item.getImageUrl())
              ))
              .collect(Collectors.toList());

      return new ExchangeHistoriesResponseDTO(
              history.getCreatedAt(),
              exchangePost.getUser().getName(),
              exchangePost.getUser().getAddress(),
              exchangePost.getUser().getProfileImage(),
              myItems,
              selectedBid.getUser().getName(),
              selectedBid.getUser().getAddress(),
              selectedBid.getUser().getProfileImage(),
              otherUserItems
      );
    }).collect(Collectors.toList());
  }


}
