package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import kosta.main.users.dto.response.UserCommentResponseDTO;
import kosta.main.users.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CommentParentDTO{
    private Integer commentId;

    private String content;
    private Boolean isOwner;

    private UserCommentResponseDTO profile;
    private String date;



    private List<CommentChildDTO> children = new ArrayList<>();

    public CommentParentDTO(Integer commentId, String content,Boolean isOwner,UserCommentResponseDTO profile) {
        this.commentId = commentId;
        this.content = content;
        this.isOwner = isOwner;
        this.profile = profile;
    }

    public static CommentParentDTO from(Comment comment, Integer userId){
        return new CommentParentDTO(comment.getCommentId(), comment.getContent(), Objects.equals(userId, comment.getUser().getUserId()), UserCommentResponseDTO.from(comment.getUser()));
    }

    public void addChild(CommentChildDTO childDTO){
        this.children.add(childDTO);
    }


}
