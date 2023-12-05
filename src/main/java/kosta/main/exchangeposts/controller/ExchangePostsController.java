package kosta.main.exchangeposts.controller;

import kosta.main.communityposts.dto.CommunityPostListDto;
import kosta.main.exchangeposts.dto.*;
import kosta.main.exchangeposts.service.ExchangePostsService;
import kosta.main.global.dto.PageInfo;
import kosta.main.global.dto.PageResponseDto;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    return new ResponseEntity(exchangePostsService.createExchangePost(user ,exchangePostDTO), HttpStatus.CREATED);
  }
  @GetMapping//필요한 데이터만 클라이언트 측으로 전송하도록 변경(23.11.27)
  public ResponseEntity<?> getAllExchangePosts(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<ExchangePostListDTO> allExchangePosts = exchangePostsService.findAllExchangePosts(pageable);
    List<ExchangePostListDTO> list = allExchangePosts.stream().toList();
    return new ResponseEntity(new PageResponseDto<>(list,PageInfo.of(allExchangePosts)), HttpStatus.OK);
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
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }


}
