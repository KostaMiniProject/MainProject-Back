package kosta.main.exchangeposts.repository;

import kosta.main.exchangeposts.entity.ExchangePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangePostsRepository extends JpaRepository<ExchangePost, Integer> {

}
