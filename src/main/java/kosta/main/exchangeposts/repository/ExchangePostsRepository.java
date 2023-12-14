package kosta.main.exchangeposts.repository;

import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.items.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangePostsRepository extends JpaRepository<ExchangePost, Integer> {

    @Query("SELECT ep FROM ExchangePost ep " +
            "WHERE (LOWER(ep.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR (ep.item.category.categoryName = :keyword) " +
            "ORDER BY ep.exchangePostId DESC")
    Page<ExchangePost> searchExchangePost(@Param("keyword") String keyword, Pageable pageable);

}
