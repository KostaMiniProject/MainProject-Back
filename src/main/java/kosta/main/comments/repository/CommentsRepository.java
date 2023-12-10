package kosta.main.comments.repository;

import kosta.main.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment, Integer>, CommentsRepositoryCustom{
}
