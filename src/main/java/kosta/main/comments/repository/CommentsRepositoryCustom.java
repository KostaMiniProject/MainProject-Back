package kosta.main.comments.repository;

import kosta.main.comments.dto.CommentDTO;
import kosta.main.comments.entity.Comment;

import java.util.List;

public interface CommentsRepositoryCustom {


    List<Comment> findComments(Integer postId);
}
