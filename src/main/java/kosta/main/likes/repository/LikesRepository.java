package kosta.main.likes.repository;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.likes.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByCommunityPost(CommunityPost communityPost);
}
