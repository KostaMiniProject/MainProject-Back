package kosta.main.reviews.service;

import kosta.main.reviews.dto.ReviewSaveDto;
import kosta.main.reviews.entity.Review;
import kosta.main.reviews.repository.ReviewsRepository;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ReviewsService {
  private final ReviewsRepository reviewsRepository;

  public void addReviews(ReviewSaveDto reviewSaveDto, User reviewer) {
//    # sudo 코드
//    1. Controller에서 ReviewSaveDto값을 받아온다.
//      (reviewer, rate, review)
//    2. ReviewSaveDto 내에서 getter을 이용해 추가하고 싶은 값을 꺼낸다.
//      (reviewedUserId, rating, review)
//    3. @LoginUser을 통해 받아온 값을 꺼낸다.(reviewer)
    //reviewedUser
//    4. Review 형인 newReview 객체에 빌드 패턴을 통해 (reviewer, reviewedUser, rating, review)값을 넣어준다.
//    5. ReviewRepository의 save 메서드를 이용해서 newReview를 DB에 적용시켜준다.


    Review newReview = Review.builder()
        .reviewer(reviewer)
        .reviewedUser(reviewSaveDto.getReviewer())
        .rating(reviewSaveDto.getRating())
        .review(reviewSaveDto.getReview())
        .build();

    reviewsRepository.save(newReview);

  }


}
