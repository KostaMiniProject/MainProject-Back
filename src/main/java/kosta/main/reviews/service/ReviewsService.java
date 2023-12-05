package kosta.main.reviews.service;

import kosta.main.items.entity.Item;
import kosta.main.reviews.dto.ReviewSaveDto;
import kosta.main.reviews.entity.Review;
import kosta.main.reviews.repository.ReviewsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import kosta.main.users.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ReviewsService {
  private final ReviewsRepository reviewsRepository;
  private final UsersService usersService;

  public void addReviews(ReviewSaveDto reviewSaveDto, User reviewer) {
//    # sudo 코드
//    1. Controller에서 ReviewSaveDto값을 받아온다.
//      (reviewer, rate, review)
//    2. ReviewSaveDto 내에서 getter을 이용해 받아온 값을 꺼낸다.
//      (rating, review)
//    3. @LoginUser을 통해 로그인한 사용자의 값을 꺼낸다.(reviewer)
//    4. UsersService의 getFindByReviewedUserId 메서드와 ReviewSaveDto 내에서 받아온 reviewedUserId를 통해
//       받아온 객체를 변수 reviewedUser에 넣어준다.
//    5. Review 형인 newReview 객체에 빌드 패턴을 통해 (reviewer, reviewedUser, rating, review)값을 넣어준다.
//    6. ReviewRepository의 save 메서드를 이용해서 newReview를 DB에 적용시켜준다.

    User reviewedUser = usersService.findUserByUserId(reviewSaveDto.getReviewedUserId());

    Review newReview = Review.builder()
        .reviewer(reviewer)
        .reviewedUser(reviewedUser)
        .rating(reviewSaveDto.getRating())
        .review(reviewSaveDto.getReview())
        .build();

    reviewsRepository.save(newReview);

  }


}
