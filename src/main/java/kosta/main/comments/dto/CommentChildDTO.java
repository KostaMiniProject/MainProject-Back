package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import kosta.main.users.dto.response.UserCommentResponseDTO;
import kosta.main.users.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentChildDTO {

    private Integer commentId;

    private String content;

    private Boolean isOwner;

    private UserCommentResponseDTO profile;

    public static CommentChildDTO from(Comment comment, Integer userId){
        String replyContent = "@" + comment.getUser().getName()+ " " + comment.getContent();
        return new CommentChildDTO(comment.getCommentId(), replyContent, Objects.equals(userId, comment.getUser().getUserId()),UserCommentResponseDTO.from(comment.getUser()));
    }
}
