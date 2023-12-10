package kosta.main.communityposts.repository;

import kosta.main.communityposts.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityPostsRepository extends JpaRepository<CommunityPost,Integer> {
    @Query("SELECT cp FROM CommunityPost cp WHERE cp.title LIKE %:keyword%")
    List<CommunityPost> findAllTitleContaining(@Param("keyword") String keyword);
}
