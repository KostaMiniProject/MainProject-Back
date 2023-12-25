package kosta.main.reviews.service;

import kosta.main.bids.entity.Bid;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.exchangeposts.repository.ExchangePostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.reviews.dto.ReviewSaveDTO;
import kosta.main.reviews.entity.Review;
import kosta.main.reviews.repository.ReviewsRepository;
import kosta.main.users.entity.User;
import kosta.main.users.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ReviewsService {
  private final ReviewsRepository reviewsRepository;
  private final UsersService usersService;
  private final ExchangePostsRepository exchangePostsRepository;

  public void addReviews(ReviewSaveDTO reviewSaveDto, User reviewer, Integer exchangePostId) {
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
    //리뷰 유저 존재하는지 확인
    reviewer = usersService.findUserByUserId(reviewer.getUserId());
    Integer reviewerId = reviewer.getUserId();
    Optional<ExchangePost> byId = exchangePostsRepository.findById(exchangePostId);
    ExchangePost exchangePost = byId.orElseThrow(() -> new BusinessException(CommonErrorCode.EXCHANGE_POST_NOT_FOUND));
//해당 유저가 해당 거래(exchangePost)에 대한 리뷰를 작성했는지,
    Optional<Review> first = reviewer.getReviews().stream().filter(review -> Objects.equals(review.getExchangePostId(),exchangePostId)).findFirst();
    if(first.isPresent()) throw new BusinessException(CommonErrorCode.ALREADY_WRITE_REVIEW);
// 그리고 리뷰를 작성하지 않았다면 거래가 완료된 입찰자나, 게시자 중 한명인지를 점검하고 리뷰를 작성할 수 있게 만들
    if(!exchangePost.getExchangePostStatus().equals(ExchangePost.ExchangePostStatus.COMPLETED))
      throw new BusinessException(CommonErrorCode.NOT_FINISHED_EXCHANGE);
    if(!Objects.equals(exchangePost.getUser().getUserId(), reviewerId) && exchangePost.getBids().stream().filter(b -> b.getStatus().equals(Bid.BidStatus.COMPLETED) && Objects.equals(b.getUser().getUserId(), reviewerId)).findFirst().isEmpty())
      throw new BusinessException(CommonErrorCode.NOT_JOIN_EXCHANGE);

    User reviewedUser = usersService.findUserByUserId(reviewSaveDto.getReviewedUserId());
    //현재 유저 점수와 리뷰 개수를 통해 리뷰 점수 평균치 계산 로직 필요
    Review newReview = Review.builder()
            .reviewer(reviewer)
            .exchangePostId(exchangePostId)
            .reviewedUser(reviewedUser)
            .rating(reviewSaveDto.getRating())
            .review(reviewSaveDto.getReview())
            .build();
    //유저에 리뷰 저장
    reviewedUser.updateRating(reviewSaveDto.getRating());
    reviewer.updateReviews(newReview);

    reviewsRepository.save(newReview);

  }


}
