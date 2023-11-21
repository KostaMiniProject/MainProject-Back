package kosta.main.reports.entity;

import jakarta.persistence.*;
import kosta.main.communityposts.entity.CommunityPosts;
import kosta.main.users.entity.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private Users reporter;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private Users reportedUser;

    @ManyToOne
    @JoinColumn(name = "reported_post_id")
    private CommunityPosts reportedPost;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(length = 20)
    private String result;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 게터와 세터
    // 생략...
}
