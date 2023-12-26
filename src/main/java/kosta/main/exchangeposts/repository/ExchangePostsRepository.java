package kosta.main.exchangeposts.repository;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.exchangeposts.entity.ExchangePost;
import kosta.main.users.entity.User;
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
        "WHERE (LOWER(ep.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
        "(ep.item.category.categoryName = :keyword) OR " +
        "(LOWER(ep.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
        "ORDER BY ep.exchangePostId DESC")
    Page<ExchangePost> searchExchangePost(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT ep FROM ExchangePost ep " +
        "WHERE ep.user.userId = :userId AND " +
        "((LOWER(ep.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR " +
        "(ep.item.category.categoryName = :keyword) OR " +
        "(LOWER(ep.content) LIKE LOWER(CONCAT('%', :keyword, '%')))) " +
        "ORDER BY ep.exchangePostId DESC")
    Page<ExchangePost> searchMyExchangePost(@Param("keyword") String keyword, @Param("userId") Integer userId, Pageable pageable);

    @Query(value = "SELECT ep FROM ExchangePost ep WHERE (ep.exchangePostStatus = 0 OR ep.exchangePostStatus = 1) AND ep.latitude IS NOT NULL AND ep.longitude IS NOT NULL AND ST_Distance_Sphere(point(ep.longitude, ep.latitude), point(:lon, :lat)) < 5000")
    List<ExchangePost> findActivePostsWithinDistance(@Param("lat") double lat, @Param("lon") double lon);


    @Query("SELECT ep FROM ExchangePost ep " +
            "JOIN ep.bids bid " +
            "WHERE ep.exchangePostStatus = 2 " +
            "AND (ep.user.userId = :userId OR bid.user.userId = :userId " +
            "AND bid.status = 5)")
    Page<ExchangePost> findCompletedExchangePostsByUserId(@Param("userId") Integer userId,Pageable pageable);
    Page<ExchangePost> findByUser_UserId(Pageable pageable, Integer userId);

}


