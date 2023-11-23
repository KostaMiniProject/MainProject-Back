package kosta.main.exchangeposts.service;

import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.items.entity.Item;
import kosta.main.items.repository.ItemsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExchangePostsService {

  private final ExchangePostsRepository exchangePostRepository;
  private final UsersRepository usersRepository;
  private final ItemsRepository itemsRepository;

  public ExchangePost createExchangePost(ExchangePostDTO exchangePostDTO) {
    User user = usersRepository.findById(exchangePostDTO.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));
    Item item = itemsRepository.findById(exchangePostDTO.getItemId())
        .orElseThrow(() -> new RuntimeException("Item not found"));

    ExchangePost exchangePost = ExchangePost.builder()
        .user(user)
        .item(item)
        .title(exchangePostDTO.getTitle())
        .preferItems(exchangePostDTO.getPreferItems())
        .address(exchangePostDTO.getAddress())
        .content(exchangePostDTO.getContent())
        .exchangePostStatus(exchangePostDTO.getExchangePostStatus())
        .build();

    return exchangePostRepository.save(exchangePost);
  }

  public List<ExchangePost> findAllExchangePosts() {
    return exchangePostRepository.findAll();
  }

  public ExchangePost updateExchangePost(Integer exchangePostId, ExchangePostDTO exchangePostDTO) {
    ExchangePost existingPost = findExchangePostById(exchangePostId);

    if (existingPost == null) {
      throw new RuntimeException("ExchangePost not found");
    }

    // 기존 값들을 기본값으로 사용, 만약 값이 넘어온다면 넘어온값을 사용함
    String title = exchangePostDTO.getTitle() != null ? exchangePostDTO.getTitle() : existingPost.getTitle();
    String preferItems = exchangePostDTO.getPreferItems() != null ? exchangePostDTO.getPreferItems() : existingPost.getPreferItems();
    String address = exchangePostDTO.getAddress() != null ? exchangePostDTO.getAddress() : existingPost.getAddress();
    String content = exchangePostDTO.getContent() != null ? exchangePostDTO.getContent() : existingPost.getContent();
    ExchangePost.ExchangePostStatus status = exchangePostDTO.getExchangePostStatus() != null ? exchangePostDTO.getExchangePostStatus() : existingPost.getExchangePostStatus();

    // Item 객체 가져오기 (필요한 경우)
    Item item = null;
    if (exchangePostDTO.getItemId() != null) {
      item = itemsRepository.findById(exchangePostDTO.getItemId())
              .orElseThrow(() -> new RuntimeException("Item not found"));
    } else {
      item = existingPost.getItem(); // 기존 item 유지
    }

    // ExchangePost 업데이트
    existingPost.updateExchangePost(
            item,
            title,
            preferItems,
            address,
            content,
            status
    );

    return exchangePostRepository.save(existingPost);
  }
  public ExchangePost findExchangePostById(Integer exchangePostId) { // 기존에 Optional로 처리하던 NUll을 수동으로 처리
    return exchangePostRepository.findById(exchangePostId)
            .orElseThrow(() -> new RuntimeException("물물교환게시글을 찾을 수 없습니다.")); // or .orElseThrow(...)
  }

}
