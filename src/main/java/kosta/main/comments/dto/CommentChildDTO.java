package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import kosta.main.users.dto.response.UserCommentResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentChildDTO {

    private Integer commentId;

    private String content;

    private UserCommentResponseDTO profile;

    public static CommentChildDTO from(Comment comment){
        String replyContent = "@" + comment.getUser().getName()+ " " + comment.getContent();
        return new CommentChildDTO(comment.getCommentId(), replyContent,UserCommentResponseDTO.from(comment.getUser()));
    }
}
