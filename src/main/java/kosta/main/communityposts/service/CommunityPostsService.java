package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.CommunityPostCreateDto;
import kosta.main.communityposts.dto.CommunityPostUpdateDto;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public CommunityPost addPost(CommunityPostCreateDto communityPostCreateDto) {
        CommunityPost communityPost = CommunityPost.builder()
                .title(communityPostCreateDto.getTitle())
                .content(communityPostCreateDto.getContent())
                .imageUrl(communityPostCreateDto.getImageUrl())
                .communityPostStatus(CommunityPost.CommunityPostStatus.PUBLIC)
                .build();
        return communityPostsRepository.save(communityPost);
    }

    /* 커뮤니티 게시글 수정 */
    public CommunityPost updatePost(Integer communityPostId, CommunityPostUpdateDto communtiyPostUpdateDto) throws Exception {
        communityPostsRepository.findById(communityPostId).orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));
        CommunityPost communityPost = CommunityPost.builder()
                .title(communtiyPostUpdateDto.getTitle())
                .content(communtiyPostUpdateDto.getContent())
                .imageUrl(communtiyPostUpdateDto.getImageUrl())
                .communityPostStatus(CommunityPost.CommunityPostStatus.PUBLIC)
                        .build();
        return communityPostsRepository.save(communityPost);
    }

    /* 커뮤니티 게시글 삭제 */
//    public void deletePost(Integer communityPostId) throws Exception {
//        CommunityPost communityPost = communityPostsRepository.findById(communityPostId).orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));
//        communityPost.setCommunityPostStatus(CommunityPost.CommunityPostStatus.DELETED);
//        communityPostsRepository.save(communityPost);
//    }
}
