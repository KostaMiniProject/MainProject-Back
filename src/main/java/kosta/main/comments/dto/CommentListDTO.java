package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentListDTO {
    private Integer commentId;

    private String content;

    private Comment.CommentStatus commentStatus;

    public static CommentListDTO from (Comment comment) {
        return new CommentListDTO(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCommentStatus()
        );
    }
}
