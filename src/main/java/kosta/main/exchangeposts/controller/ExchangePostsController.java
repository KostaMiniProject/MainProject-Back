package kosta.main.exchangeposts.controller;

import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.service.ExchangePostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  @GetMapping("/{exchangePostId}") // 정상 동작 확인 완료
  public ResponseEntity getExchangePostById(@PathVariable Integer exchangePostId) {
    ExchangePost exchangePost = exchangePostsService.findExchangePostById(exchangePostId);
    return new ResponseEntity(exchangePost, HttpStatus.OK);
    // 23.11.23 exchangePostId와 같은값은 굳이 프론트로 노출시킬 이유가 없기 때문에 리팩터링때
    // DTO를 개별적으로 생성한뒤 필요한 데이터만 보내기로 했다.
  }
  @PutMapping("/{exchangePostId}") // 정상 동작 확인 완료
  public ResponseEntity<ExchangePost> updateExchangePost(@PathVariable Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
    ExchangePost updatedPost = exchangePostsService.updateExchangePost(exchangePostId, exchangePostDTO);
    return ResponseEntity.ok(updatedPost);
  }
  @DeleteMapping("/{exchangePostId}") // softdelete로 수정 완료, 정상 동작 확인
  public ResponseEntity<ExchangePost> deleteExchangePost(@PathVariable Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
      ExchangePost updatedPost = exchangePostsService.updateExchangePost(exchangePostId, exchangePostDTO);
      return ResponseEntity.ok(updatedPost);
  }
  @PostMapping// 정상 동작 확인 완료
  public ExchangePost createExchangePost(@RequestBody ExchangePostDTO exchangePostDTO) {
    return exchangePostsService.createExchangePost(exchangePostDTO);
  }

}
