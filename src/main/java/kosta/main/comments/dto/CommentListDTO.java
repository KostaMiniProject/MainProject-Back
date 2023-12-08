package kosta.main.comments.dto;

import kosta.main.comments.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListDTO implements Serializable {
    private Integer commentId;

    private String content;

    private Integer userId;


    private List<CommentListDTO> children = new ArrayList<>();

    public CommentListDTO(Integer id, String content, Integer userId) {
        this.commentId = id;
        this.content = content;
        this.userId = userId;
    }

    public static CommentListDTO convertCommentToDto(Comment comment) {
        return comment.getCommentStatus() == Comment.CommentStatus.DELETED ?
                new CommentListDTO(comment.getCommentId(), "삭제된 댓글입니다.", null) :
                new CommentListDTO(comment.getCommentId(), comment.getContent(), comment.getUser().getUserId());
    }
}
