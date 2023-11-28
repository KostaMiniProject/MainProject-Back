package kosta.main.exchangeposts.service;

import kosta.main.bids.repository.BidRepository;
import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.dto.ExchangePostDetailDTO;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
import kosta.main.exchangeposts.dto.ExchangePostResponseDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExchangePostsService {

  private final ExchangePostsRepository exchangePostRepository;
  private final UsersRepository usersRepository;
  private final ItemsRepository itemsRepository;
  private final BidRepository bidRepository;

  @Transactional
  public ExchangePostDTO createExchangePost(ExchangePostDTO exchangePostDTO) {
    // 사용자와 아이템을 찾는 과정
    User user = usersRepository.findById(exchangePostDTO.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));
    Item item = itemsRepository.findById(exchangePostDTO.getItemId())
        .orElseThrow(() -> new RuntimeException("Item not found"));

    // ExchangePost 엔티티 생성
    ExchangePost exchangePost = ExchangePost.builder()
        .user(user)
        .item(item)
        .title(exchangePostDTO.getTitle())
        .preferItems(exchangePostDTO.getPreferItems())
        .address(exchangePostDTO.getAddress())
        .content(exchangePostDTO.getContent())
        .exchangePostStatus(exchangePostDTO.getExchangePostStatus())
        .build();

    // 엔티티 저장
    ExchangePost savedExchangePost = exchangePostRepository.save(exchangePost);

    // DTO로 변환하여 반환
    return new ExchangePostDTO(
        savedExchangePost.getTitle(),
        savedExchangePost.getPreferItems(),
        savedExchangePost.getAddress(),
        savedExchangePost.getContent(),
        savedExchangePost.getUser().getUserId(),
        savedExchangePost.getItem().getItemId(),
        savedExchangePost.getExchangePostStatus()
    );
  }

  @Transactional(readOnly = true)
  public List<ExchangePostListDTO> findAllExchangePosts() {
    return exchangePostRepository.findAll().stream()
        .map(post -> {
          // 아이템 대표 이미지 URL을 가져오는 로직
          String imgUrl = post.getItem() != null ? post.getItem().getImageUrl() : null;
          //해당 교환 게시글에 입찰된 Bid의 갯수를 세는 로직 + Bidstatus가 delete인건  세지 않도록 하는 로직
          Integer bidCount = bidRepository.countByExchangePostAndStatusNotDeleted(post);

          return ExchangePostListDTO.builder()
              .exchangePostId(post.getExchangePostId())
              .title(post.getTitle())
              .preferItem(post.getPreferItems())
              .address(post.getAddress())
              .exchangePostStatus(post.getExchangePostStatus().toString())
              .created_at(post.getCreatedAt())
              .imgUrl(imgUrl)
              .bidCount(bidCount)
              .build();
        })
        .collect(Collectors.toList());
  }


  @Transactional(readOnly = true)
  public ExchangePostDetailDTO findExchangePostById(Integer exchangePostId) {
    ExchangePost post = exchangePostRepository.findById(exchangePostId)
        .orElseThrow(() -> new RuntimeException("ExchangePost not found"));

    return ExchangePostDetailDTO.builder()
        .exchangePostId(post.getExchangePostId())
        .userId(post.getUser().getUserId())
        .userName(post.getUser().getName())
        .itemId(post.getItem().getItemId())
        .itemTitle(post.getItem().getTitle())
        .title(post.getTitle())
        .preferItems(post.getPreferItems())
        .address(post.getAddress())
        .content(post.getContent())
        .exchangePostStatus(post.getExchangePostStatus().toString())
        .build();
  }

  @Transactional
  public ExchangePostResponseDTO updateExchangePost(Integer exchangePostId, ExchangePostDTO updatedExchangePostDTO) {
    ExchangePost existingExchangePost = exchangePostRepository.findById(exchangePostId)
        .orElseThrow(() -> new RuntimeException("ExchangePost not found"));

    // 필요한 경우 User 및 Item 엔티티 조회
    User user = updatedExchangePostDTO.getUserId() != null
        ? usersRepository.findById(updatedExchangePostDTO.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"))
        : existingExchangePost.getUser();

    Item item = updatedExchangePostDTO.getItemId() != null
        ? itemsRepository.findById(updatedExchangePostDTO.getItemId())
        .orElseThrow(() -> new RuntimeException("Item not found"))
        : existingExchangePost.getItem();

    // ExchangePost 엔티티 업데이트
    existingExchangePost.updateWithBuilder(user, item, updatedExchangePostDTO); //빌더를 이용한 수정
    return ExchangePostResponseDTO.builder()
        .exchangePostId(existingExchangePost.getExchangePostId())
        .userId(user.getUserId())
        .itemId(item.getItemId())
        .title(existingExchangePost.getTitle())
        .preferItems(existingExchangePost.getPreferItems())
        .address(existingExchangePost.getAddress())
        .content(existingExchangePost.getContent())
        .exchangePostStatus(existingExchangePost.getExchangePostStatus().toString())
        .build();
  }

  @Transactional
  public void deleteExchangePost(Integer exchangePostId) {
    ExchangePost existingExchangePost = exchangePostRepository.findById(exchangePostId)
        .orElseThrow(() -> new RuntimeException("ExchangePost not found"));

    ExchangePost newExchangePost = ExchangePost.builder()
        .exchangePostId(existingExchangePost.getExchangePostId())
        .user(existingExchangePost.getUser())
        .item(existingExchangePost.getItem())
        .title(existingExchangePost.getTitle())
        .preferItems(existingExchangePost.getPreferItems())
        .address(existingExchangePost.getAddress())
        .content(existingExchangePost.getContent())
        .exchangePostStatus(ExchangePost.ExchangePostStatus.DELETED)
        .build();
    exchangePostRepository.save(newExchangePost);
  }

}
