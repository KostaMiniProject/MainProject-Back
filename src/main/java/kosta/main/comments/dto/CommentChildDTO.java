package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentChildDTO {

    private Integer commentId;

    private String content;

    private Integer userId;

    public static CommentChildDTO from(Comment comment){
        String replyContent = "@" + comment.getUser().getName()+ " " + comment.getContent();
        return new CommentChildDTO(comment.getCommentId(), replyContent, comment.getUser().getUserId());
    }
}
