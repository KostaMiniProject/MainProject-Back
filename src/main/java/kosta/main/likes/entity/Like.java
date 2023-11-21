package kosta.main.likes.entity;

import jakarta.persistence.*;
import kosta.main.audit.Auditable;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
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
    @JoinColumn(name = "community_post_id")
    private CommunityPost communityPost;

    @CreatedDate
    @Column
    private LocalDateTime createdAt = LocalDateTime.now();
    // 게터와 세터
    // 생략...
}
