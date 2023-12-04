package kosta.main.reviews.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Review extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer; // 평가를 하는 사람(입찰자)

    @ManyToOne
    @JoinColumn(name = "reviewed_user_id")
    private User reviewedUser; // 평가를 받는 사람(게시자)

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String review;


    // 게터와 세터
    // 생략...
}
