package kosta.main.comments.entity;

import jakarta.persistence.*;
import kosta.main.comments.dto.CommentUpdateDTO;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.global.audit.Auditable;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "community_post_id")
    private CommunityPost communityPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>(); //자식 댓글

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 10)
    @Builder.Default
    private Comment.CommentStatus commentStatus = CommentStatus.PUBLIC;

    public void updateChild(Comment comment) {
        this.children.add(comment);
    }

    public enum CommentStatus {
        PUBLIC, REPORTED, DELETED
    }

    public void updateComment(CommentUpdateDTO commentUpdateDTO) {
        this.content = commentUpdateDTO.getContent() != null && !commentUpdateDTO.getContent().equals(this.content) ? commentUpdateDTO.getContent() : this.content;
    }

    // 부모 댓글 수정
    public void updateParent(Comment parent){
        this.parent = parent;
    }

    public void updateCommentStatus(Comment.CommentStatus commentStatus) {
        this.commentStatus = commentStatus;
    }

    // 부모 댓글을 삭제해도 자식 댓글은 남아있음
//    @OneToMany(mappedBy = "parent")
//    private List<Comment> childList = new ArrayList<>();

    // 게터와 세터
    // 생략...

}
