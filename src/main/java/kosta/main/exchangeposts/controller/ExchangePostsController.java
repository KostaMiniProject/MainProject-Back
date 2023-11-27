package kosta.main.exchangeposts.controller;

import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.service.ExchangePostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchange-posts")
@RequiredArgsConstructor
public class ExchangePostsController {

  private final ExchangePostsService exchangePostsService;

  @GetMapping// 정상동작 확인완료
  public List<ExchangePost> getAllExchangePosts() {
    return exchangePostsService.findAllExchangePosts();
  }

  @GetMapping("/{exchangePostId}") // 정상 동작 확인 완료
  public ResponseEntity getExchangePostById(@PathVariable("exchangePostId") Integer exchangePostId) {
    return ResponseEntity.ok(exchangePostsService.findExchangePostById(exchangePostId));
    // 23.11.23 exchangePostId와 같은값은 굳이 프론트로 노출시킬 이유가 없기 때문에 리팩터링때
    // DTO를 개별적으로 생성한뒤 필요한 데이터만 보내기로 했다.
  }
  @PutMapping("/{exchangePostId}") // 정상 동작 확인 완료
  public ResponseEntity<ExchangePost> updateExchangePost(@PathVariable("exchangePostId") Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
    return ResponseEntity.ok(exchangePostsService.updateExchangePost(exchangePostId, exchangePostDTO));
  }
  @DeleteMapping("/{exchangePostId}") // softdelete로 수정 완료, 정상 동작 확인
  public ResponseEntity<ExchangePost> deleteExchangePost(@PathVariable("exchangePostId") Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
      return ResponseEntity.ok(exchangePostsService.updateExchangePost(exchangePostId, exchangePostDTO));
  }
  @PostMapping// 정상 동작 확인 완료
  public ExchangePost createExchangePost(@RequestBody ExchangePostDTO exchangePostDTO) {
    return exchangePostsService.createExchangePost(exchangePostDTO);
  }

}
