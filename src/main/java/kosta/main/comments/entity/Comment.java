package kosta.main.comments.entity;

import jakarta.persistence.*;
import kosta.main.global.audit.Auditable;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@SQLDelete(sql = "UPDATE comments SET comment_status = 2 WHERE community_post_id = ?")
@Where(clause = "comment_status = 'PUBLIC'") //문제있을수도있음
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "community_post_id")
    private CommunityPost communityPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 10)
    @Builder.Default
    private Comment.CommentStatus commentStatus = CommentStatus.PUBLIC;
    public enum CommentStatus {
        PUBLIC, REPORTED, DELETED
    }
    // 게터와 세터
    // 생략...
}
