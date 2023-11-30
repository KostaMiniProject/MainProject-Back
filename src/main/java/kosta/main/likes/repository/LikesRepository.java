package kosta.main.likes.repository;

import kosta.main.likes.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Like, Integer> {
}
