package kosta.main.reviews.controller;


import kosta.main.reviews.dto.ReviewSaveDto;
import kosta.main.reviews.entity.Review;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import kosta.main.reviews.service.ReviewsService;

@RestController
@RequiredArgsConstructor
public class ReviewsController {
  private final ReviewsService reviewsService;

  // 리뷰 작성하기
  @PostMapping("/users/reviews")
//  TODO @LoginUser 적용
  public void addReviews(@RequestBody ReviewSaveDto reviewSaveDto, User reviewer) {
//    TODO @LoginUser 값을 받아와서 new User() 수정
    reviewsService.addReviews(reviewSaveDto, new User());
  }

}
