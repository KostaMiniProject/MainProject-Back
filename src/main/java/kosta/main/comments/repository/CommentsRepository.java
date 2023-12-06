package kosta.main.comments.repository;

import kosta.main.comments.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepository extends CrudRepository<Comment, Integer> {
}
