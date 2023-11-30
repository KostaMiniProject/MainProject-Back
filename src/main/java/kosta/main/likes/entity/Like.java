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
@Builder
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

    public void setCommunityPost(CommunityPost communityPost) {
        this.communityPost = communityPost;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // 게터와 세터
    // 생략...
}
