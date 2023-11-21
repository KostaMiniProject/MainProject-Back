package kosta.main.reviews.entity;

import jakarta.persistence.*;
import kosta.main.users.entity.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private Users reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewed_user_id")
    private Users reviewedUser;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 게터와 세터
    // 생략...
}
