package kosta.main.reviews.service;

import kosta.main.exchangehistories.entity.ExchangeHistory;
import kosta.main.exchangehistories.repository.ExchangeHistoriesRepository;
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
  private final ExchangeHistoriesRepository exchangeHistoriesRepository;
  private final UsersService usersService;

  public void addReviews(ReviewSaveDTO reviewSaveDto, User reviewer, Integer exchangeHistoryId) {
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
    if(reviewer == null) throw new BusinessException(CommonErrorCode.USER_NOT_FOUND);
    Integer reviewerId = reviewer.getUserId();
//해당 유저가 해당 거래(ExchangeHistory)에 대한 리뷰를 작성했는지,
    Optional<Review> first = reviewer.getReviews().stream().filter(review -> Objects.equals(review.getExchangeHistoryId(), exchangeHistoryId)).findFirst();
    if(first.isPresent()) throw new BusinessException(CommonErrorCode.ALREADY_WRITE_REVIEW);
// 그리고 리뷰를 작성하지 않았다면 거래가 완료된 입찰자나, 게시자 중 한명인지를 점검하고 리뷰를 작성할 수 있게 만들
    Optional<ExchangeHistory> byId = exchangeHistoriesRepository.findById(exchangeHistoryId);
    ExchangeHistory exchangeHistory = byId.orElseThrow(() -> new BusinessException(CommonErrorCode.EXCHANGE_HISTORY_NOT_FOUND));
    if(!Objects.equals(exchangeHistory.getExchangeInitiator().getUserId(), reviewerId) && !Objects.equals(exchangeHistory.getExchangePartner().getUserId(), reviewerId))
      throw new BusinessException(CommonErrorCode.NOT_JOIN_EXCHANGE);

    User reviewedUser = usersService.findUserByUserId(reviewSaveDto.getReviewedUserId());

    //현재 유저 점수와 리뷰 개수를 통해 리뷰 점수 평균치 계산 로직 필요
    Review newReview = Review.builder()
        .reviewer(reviewer)
            .exchangeHistoryId(exchangeHistoryId)
        .reviewedUser(reviewedUser)
        .rating(reviewSaveDto.getRating())
        .review(reviewSaveDto.getReview())
        .build();
    //유저에 리뷰 저장
    reviewer.updateReviews(newReview);

    reviewsRepository.save(newReview);

  }


}
