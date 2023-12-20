package kosta.main.exchangeposts.repository;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.exchangeposts.entity.ExchangePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangePostsRepository extends JpaRepository<ExchangePost, Integer> {

    @Query("SELECT ep FROM ExchangePost ep " +
            "WHERE (LOWER(ep.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR (ep.item.category.categoryName = :keyword) " +
            "ORDER BY ep.exchangePostId DESC")
    Page<ExchangePost> searchExchangePost(@Param("keyword") String keyword, Pageable pageable);
    @Query(value = "SELECT * FROM exchange_posts e WHERE ST_Distance_Sphere(point(e.longitude, e.latitude), point(:lon, :lat)) < 5000", nativeQuery = true)
    List<ExchangePost> findPostsWithinDistance(@Param("lat") double lat, @Param("lon") double lon);

    Page<ExchangePost> findByUser_UserId(Pageable pageable, Integer userId);

}


