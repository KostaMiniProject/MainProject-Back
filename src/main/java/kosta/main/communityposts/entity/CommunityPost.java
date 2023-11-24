package kosta.main.communityposts.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.users.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommunityPost extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communityPostId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Integer views;

    @Column(length = 20, nullable = false)
    private CommunityPostStatus communityPostStatus = CommunityPostStatus.PUBLIC;

    @Column(length = 255)
    private String imageUrl;


    // 게터와 세터
    // 생략...

    public enum CommunityPostStatus {
        PUBLIC, PRIVATE, REPORTED, DELETED
    }
}