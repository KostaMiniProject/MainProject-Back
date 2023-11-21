package kosta.main.communityposts.entity;

import jakarta.persistence.*;
import kosta.main.users.entity.Users;
import java.time.LocalDateTime;

@Entity
@Table(name = "community_posts")
public class CommunityPosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Integer view;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 게터와 세터
    // 생략...
}
