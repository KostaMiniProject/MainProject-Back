package kosta.main.comments.repository;

import kosta.main.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Integer>, CommentsRepositoryCustom{

    List<Comment> findCommentByUser_UserId(Integer userId);
}
