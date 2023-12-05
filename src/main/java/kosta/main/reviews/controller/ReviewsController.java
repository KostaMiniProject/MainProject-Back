package kosta.main.reviews.controller;


import kosta.main.reviews.dto.ReviewSaveDto;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import kosta.main.reviews.service.ReviewsService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewsController {
  private final ReviewsService reviewsService;

  // 리뷰 작성하기
  @PostMapping("/api/reviews")
  public void addReviews(@RequestBody ReviewSaveDto reviewSaveDto, @LoginUser User reviewer) {
//    TODO @LoginUser 값을 받아와서 new User() 수정
    log.info("{}=================================",reviewer);
    reviewsService.addReviews(reviewSaveDto, reviewer);
  }

}
