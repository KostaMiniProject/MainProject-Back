package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import kosta.main.users.dto.response.UserCommentResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CommentParentDTO{
    private Integer commentId;

    private String content;

    private UserCommentResponseDTO profile;



    private List<CommentChildDTO> children = new ArrayList<>();

    public CommentParentDTO(Integer commentId, String content,UserCommentResponseDTO profile ) {
        this.commentId = commentId;
        this.content = content;
        this.profile = profile;
    }

    public static CommentParentDTO from(Comment comment){
        return new CommentParentDTO(comment.getCommentId(), comment.getContent(), UserCommentResponseDTO.from(comment.getUser()));
    }

    public void addChild(CommentChildDTO childDTO){
        this.children.add(childDTO);
    }

}
