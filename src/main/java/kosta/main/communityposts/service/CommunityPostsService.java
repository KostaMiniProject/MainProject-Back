package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.CommuntiyPostUpdateDto;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityPostsService {
    private final CommunityPostsRepository communityPostsRepository;

    /* 커뮤니티 목록 조회 */
    @Transactional(readOnly = true)
    public List<CommunityPost> findPosts() {
        return communityPostsRepository.findAll();
    }

    /* 커뮤니티 게시글 상세 조회 */
    @Transactional(readOnly = true)
    public CommunityPost findPost(Integer communityPostId) throws Exception {
        return communityPostsRepository.findById(communityPostId).orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));
    }

    /* 커뮤니티 게시글 작성 */
    public void addPost(CommunityPost communityPost) {
        communityPostsRepository.save(communityPost);
    }

    /* 커뮤니티 게시글 수정 */
//    public void updatePost(Integer communityPostId, CommuntiyPostUpdateDto communtiyPostUpdateDto) throws Exception {
//        CommunityPost communityPost = communityPostsRepository.findById(communityPostId).orElseThrow(() -> new Exception("게시글이 존재하지 않습니다.");
//        communityPost.setTitle(communtiyPostUpdateDto.getTitle());
//    }
}
