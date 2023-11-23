package kosta.main.exchangeposts.controller;

import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.service.ExchangePostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchange-posts")
public class ExchangePostsController {

  private final ExchangePostsService exchangePostsService;

  @Autowired
  public ExchangePostsController(ExchangePostsService exchangePostsService) {
    this.exchangePostsService = exchangePostsService;
  }

  @GetMapping// 정상동작 확인완료
  public List<ExchangePost> getAllExchangePosts() {
    return exchangePostsService.findAllExchangePosts();
  }

  @GetMapping("/{exchangePostId}")
  public ResponseEntity<ExchangePost> getExchangePostById(@PathVariable Integer id) {
    ExchangePost exchangePost = exchangePostsService.findExchangePostById(id);
    if (exchangePost == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(exchangePost);
  }
  @PutMapping("/{exchangePostId}") // setter를 사용하지 않고 생성자를 활용하는 방식으로 수정
  public ResponseEntity<ExchangePost> updateExchangePost(@PathVariable Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
    try {
      ExchangePost updatedPost = exchangePostsService.updateExchangePost(exchangePostId, exchangePostDTO);
      return ResponseEntity.ok(updatedPost);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
  @DeleteMapping("/{exchangePostId}") // softdelete로 수정 완료
  public ResponseEntity<ExchangePost> deleteExchangePost(@PathVariable Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
    try {
      ExchangePost updatedPost = exchangePostsService.updateExchangePost(exchangePostId, exchangePostDTO);
      return ResponseEntity.ok(updatedPost);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }


  @PostMapping// 정상 동작 확인 완료
  public ExchangePost createExchangePost(@RequestBody ExchangePostDTO exchangePostDTO) {
    return exchangePostsService.createExchangePost(exchangePostDTO);
  }

}
