package kosta.main.exchangeposts.controller;

import kosta.main.exchangeposts.dto.*;
import kosta.main.exchangeposts.service.ExchangePostsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchange-posts")
@RequiredArgsConstructor
public class ExchangePostsController {

  private final ExchangePostsService exchangePostsService;

  @PostMapping
  public ResponseEntity<?> createExchangePost(@LoginUser User user, @RequestBody ExchangePostDTO exchangePostDTO) {
    return ResponseEntity.ok(exchangePostsService.createExchangePost(user ,exchangePostDTO));
  }
  @GetMapping//필요한 데이터만 클라이언트 측으로 전송하도록 변경(23.11.27)
  public List<ExchangePostListDTO> getAllExchangePosts() {
    return exchangePostsService.findAllExchangePosts();
  }

  @GetMapping("/{exchangePostId}")
  public ResponseEntity<ExchangePostDetailDTO> getExchangePostById(@PathVariable("exchangePostId") Integer exchangePostId, @LoginUser User user) {
    return ResponseEntity.ok(exchangePostsService.findExchangePostById(exchangePostId,user));
  }

  @PutMapping("/{exchangePostId}") //필요한 데이터만 클라이언트 측으로 전송하도록 변경(23.11.27)
  public ResponseEntity<?> updateExchangePost(@LoginUser User user,  @PathVariable("exchangePostId") Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
    return ResponseEntity.ok(exchangePostsService.updateExchangePost(user, exchangePostId, exchangePostDTO));
  }
  @DeleteMapping("/{exchangePostId}")
  public ResponseEntity<?> deleteExchangePost(@PathVariable("exchangePostId") Integer exchangePostId, @LoginUser  User user) {
    exchangePostsService.deleteExchangePost(exchangePostId, user);
    return ResponseEntity.ok().build();
  }


}
