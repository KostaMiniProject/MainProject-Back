package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
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

    private Integer userId;


    private List<CommentChildDTO> children = new ArrayList<>();

    public CommentParentDTO(Integer commentId, String content, Integer userId) {
        this.commentId = commentId;
        this.content = content;
        this.userId = userId;
    }

    public static CommentParentDTO from(Comment comment){
        return new CommentParentDTO(comment.getCommentId(), comment.getContent(), comment.getUser().getUserId());
    }

    public void addChild(CommentChildDTO childDTO){
        this.children.add(childDTO);
    }

}
