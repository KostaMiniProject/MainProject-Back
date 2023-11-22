package kosta.main.communityposts.repository;

import kosta.main.communityposts.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostsRepository extends JpaRepository<CommunityPost,Integer> {

}
