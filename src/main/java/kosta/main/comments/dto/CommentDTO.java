package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDTO {
    private Integer communityPostId;
    private Integer commentId;
    private String content;

    public CommentDTO(Comment comment) {
        this.communityPostId = comment.getCommunityPost().getCommunityPostId();
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
    }


}
