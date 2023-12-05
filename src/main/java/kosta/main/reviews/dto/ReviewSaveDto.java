package kosta.main.reviews.dto;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class ReviewSaveDto extends Auditable {
    private Integer reviewedUserId; // 평가를 하는 사람(입찰자)
    private Integer rating;
    private String review;


    // 게터와 세터
    // 생략...
}
