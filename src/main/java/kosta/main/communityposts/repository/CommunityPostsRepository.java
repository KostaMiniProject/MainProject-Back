package kosta.main.communityposts.repository;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.items.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityPostsRepository extends JpaRepository<CommunityPost,Integer> {
//    @Query("SELECT cp FROM CommunityPost cp WHERE cp.title LIKE %:keyword%")
//    List<CommunityPost> findAllTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT cp FROM CommunityPost cp " +
        "WHERE ((LOWER(cp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(cp.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND cp.communityPostStatus = 0) " +
        "   OR (cp.communityPostStatus = 1 AND cp.user.userId = :userId) " +
        "ORDER BY cp.communityPostId DESC")
    Page<CommunityPost> findAllTitleOrContentContainingByUser(@Param("keyword") String keyword, @Param("userId") Integer userId, Pageable pageable);


    @Query("SELECT cp FROM CommunityPost cp WHERE (LOWER(cp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(cp.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND cp.communityPostStatus = 0 " +
        "ORDER BY cp.communityPostId DESC")
    Page<CommunityPost> findAllTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT cp FROM CommunityPost cp " +
        "WHERE cp.user.userId = :userId AND " +
        "(LOWER(cp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(cp.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
        "AND cp.communityPostStatus = 0 " +
        "ORDER BY cp.communityPostId DESC")
    Page<CommunityPost> findByUserAndKeyword(@Param("userId") Integer userId, @Param("keyword") String keyword, Pageable pageable);

    Page<CommunityPost> findByUser_UserId(Pageable pageable, Integer userId);
    List<CommunityPost> findByUser_UserId(Integer userId);

}
