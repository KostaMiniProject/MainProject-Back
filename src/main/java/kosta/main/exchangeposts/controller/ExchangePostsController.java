package kosta.main.exchangeposts.controller;

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

  @GetMapping("/search")
  public ResponseEntity<ExchangePostDetailDTO> searchExchangePost(@RequestParam(value = "keyword") String keyword,
                                                                  @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<ExchangePostListDTO> searchExchangePosts = exchangePostsService.searchAllExchangePosts(keyword, pageable);

    List<ExchangePostListDTO> list = searchExchangePosts.stream().toList();
    return new ResponseEntity(new PageResponseDto<>(list,PageInfo.of(searchExchangePosts)), HttpStatus.OK);
  }

  @GetMapping("/search-my-exchange-posts")
  public ResponseEntity<ExchangePostDetailDTO> searchMyExchangePost(@RequestParam(value = "keyword") String keyword,
                                                                  @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                    @LoginUser User user) {
    Page<ExchangePostListDTO> searchExchangePosts = exchangePostsService.searchMyAllExchangePosts(keyword, pageable, user);

    List<ExchangePostListDTO> list = searchExchangePosts.stream().toList();
    return new ResponseEntity(new PageResponseDto<>(list,PageInfo.of(searchExchangePosts)), HttpStatus.OK);
  }


  @GetMapping("/{exchangePostId}")
  public ResponseEntity<ExchangePostDetailDTO> getExchangePostById(@PathVariable("exchangePostId") Integer exchangePostId, @LoginUser User user) {
    return ResponseEntity.ok(exchangePostsService.findExchangePostById(exchangePostId,user));
  }

  @PutMapping("/{exchangePostId}") //필요한 데이터만 클라이언트 측으로 전송하도록 변경(23.11.27)
  public ResponseEntity<?> updateExchangePost(@LoginUser User user,  @PathVariable("exchangePostId") Integer exchangePostId, @RequestBody ExchangePostDTO exchangePostDTO) {
    return new ResponseEntity<>(exchangePostsService.updateExchangePost(user, exchangePostId, exchangePostDTO), HttpStatus.OK);
  }
  @DeleteMapping("/{exchangePostId}")
  public ResponseEntity<?> deleteExchangePost(@PathVariable("exchangePostId") Integer exchangePostId, @LoginUser  User user) {
    exchangePostsService.deleteExchangePost(exchangePostId, user);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  //카카오 API 호출 테스트
  @GetMapping("/test")
  public String getLocation(@RequestBody ExchangePostDTO exchangePostDTO){
    return exchangePostsService.getLocation(exchangePostDTO.getAddress().orElse(null));
  }
  @GetMapping("/exchangePostMap")
  public List<ExchangePostListForMapDTO> getExchangePostForMap(
      @RequestParam("longitude") String longitude,
      @RequestParam("latitude") String latitude) {
    // MapDTO 객체를 생성하여 서비스에 전달
    return exchangePostsService.getExchangePostForMap(longitude, latitude);
  }

}
