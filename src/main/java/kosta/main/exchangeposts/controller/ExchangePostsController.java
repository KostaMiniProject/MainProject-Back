package kosta.main.exchangeposts.controller;

import kosta.main.exchangeposts.dto.ExchangePostDTO;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.service.ExchangePostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchangePosts")
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

  @GetMapping("/{id}")
  public ResponseEntity<ExchangePost> getExchangePostById(@PathVariable Integer id) {
    ExchangePost exchangePost = exchangePostsService.findExchangePostById(id);
    if (exchangePost == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(exchangePost);
  }


  @PostMapping// 정상 동작 확인 완료
  public ExchangePost createExchangePost(@RequestBody ExchangePostDTO exchangePostDTO) {
    return exchangePostsService.createExchangePost(exchangePostDTO);
  }

//  @PutMapping("/{id}")
//  public ResponseEntity<ExchangePost> updateExchangePost(@PathVariable Integer id, @RequestBody ExchangePostDTO exchangePostDTO) {
//    return ResponseEntity.ok(exchangePostsService.updateExchangePost(id, exchangePostDTO));
//  }

  @DeleteMapping("/{id}") // status를 변경하는 방식으로 수정예정
  public ResponseEntity<?> deleteExchangePost(@PathVariable Integer id) {
    ExchangePost exchangePost = exchangePostsService.findExchangePostById(id);
    if (exchangePost == null) {
      return ResponseEntity.notFound().build();
    }
    exchangePostsService.deleteExchangePost(id);
    return ResponseEntity.ok().build();
  }

}
