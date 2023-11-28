package kosta.main.likes.entity;

import jakarta.persistence.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_post_id", nullable = false)
    private CommunityPost communityPost;

    @CreatedDate
    @Column
    private LocalDateTime createdAt = LocalDateTime.now();
    // 게터와 세터
    // 생략...

    @Builder
    public Like(CommunityPost communityPost) {
        this.communityPost = communityPost;
    }

    public static Like of(CommunityPost communityPost) {
        Like like = Like.builder()
                .communityPost(communityPost)
                .build();
        communityPost.getLikePostList().add(like);
        return like;
    }
}
