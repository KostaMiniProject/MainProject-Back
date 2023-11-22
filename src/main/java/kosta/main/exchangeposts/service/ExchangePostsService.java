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

  public ExchangePost findExchangePostById(Integer id) { // 기존에 Optional로 처리하던 NUll을 수동으로 처리
    return exchangePostRepository.findById(id).orElse(null); // or .orElseThrow(...)
  }


//  public ExchangePost updateExchangePost(Integer id, ExchangePostDTO exchangePostDTO) {
//    return exchangePostRepository.findById(id).map(existingPost -> {
//      // 필요한 경우, User와 Item 객체도 업데이트
//      existingPost.setTitle(exchangePostDTO.getTitle());
//      existingPost.setPreferItems(exchangePostDTO.getPreferItems());
//      existingPost.setAddress(exchangePostDTO.getAddress());
//      existingPost.setContent(exchangePostDTO.getContent());
//      existingPost.setExchangePostStatus(exchangePostDTO.getExchangePostStatus());
//      return exchangePostRepository.save(existingPost);
//    }).orElseThrow(() -> new RuntimeException("ExchangePost not found"));
//  }

  public void deleteExchangePost(Integer id) {
    exchangePostRepository.deleteById(id);
  }
}
