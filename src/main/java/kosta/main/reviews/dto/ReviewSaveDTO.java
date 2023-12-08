package kosta.main.reviews.dto;

import kosta.main.global.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class ReviewSaveDTO extends Auditable {
    private Integer reviewedUserId; // 평가를 하는 사람(입찰자)
    private Integer rating;
    private String review;


    // 게터와 세터
    // 생략...
}
