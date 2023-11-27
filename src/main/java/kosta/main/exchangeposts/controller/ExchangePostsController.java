package kosta.main.exchangeposts.controller;

import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.dto.ExchangePostDetailDTO;
import kosta.main.exchangeposts.dto.ExchangePostListDTO;
import kosta.main.exchangeposts.dto.ExchangePostResponseDTO;
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

  @PostMapping
  public ExchangePostDTO createExchangePost(@RequestBody ExchangePostDTO exchangePostDTO) {
    return exchangePostsService.createExchangePost(exchangePostDTO);
  }
  @GetMapping//필요한 데이터만 클라이언트 측으로 전송하도록 변경(23.11.27)
  public List<ExchangePostListDTO> getAllExchangePosts() {
    return exchangePostsService.findAllExchangePosts();
  }

  @GetMapping("/{exchangePostId}")
  public ResponseEntity<ExchangePostDetailDTO> getExchangePostById(@PathVariable("exchangePostId") Integer exchangePostId) {
    ExchangePostDetailDTO postDetail = exchangePostsService.findExchangePostById(exchangePostId);
    return ResponseEntity.ok(postDetail);
  }

  @PutMapping("/{exchangePostId}") //필요한 데이터만 클라이언트 측으로 전송하도록 변경(23.11.27)
  public ResponseEntity<ExchangePostResponseDTO> updateExchangePost(@PathVariable("exchangePostId") Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
    return ResponseEntity.ok(exchangePostsService.updateExchangePost(exchangePostId, exchangePostDTO));
  }
  @DeleteMapping("/{exchangePostId}")
  public ResponseEntity<?> deleteExchangePost(@PathVariable("exchangePostId") Integer exchangePostId) {
    exchangePostsService.deleteExchangePost(exchangePostId);
    return ResponseEntity.ok().build();
  }


}
