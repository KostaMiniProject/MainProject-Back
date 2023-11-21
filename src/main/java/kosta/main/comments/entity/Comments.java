package kosta.main.comments.entity;

import jakarta.persistence.*;
import kosta.main.communityposts.entity.CommunityPosts;
import kosta.main.users.entity.Users;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comments parent;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private CommunityPosts post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 게터와 세터
    // 생략...
}
