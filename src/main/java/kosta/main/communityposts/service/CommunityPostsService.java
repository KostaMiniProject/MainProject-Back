package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.CommunityPostCreateDto;
import kosta.main.communityposts.dto.CommunityPostResponseDto;
import kosta.main.communityposts.dto.CommunityPostUpdateDto;
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

    /* RuntimeException 추상 메소드 */
    public CommunityPost findCommunityPostByCommunityPostId(Integer communityPostId) {
        return communityPostsRepository.findById(communityPostId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    /* 커뮤니티 목록 조회 */
    @Transactional(readOnly = true)
    public List<CommunityPost> findPosts() {
        return communityPostsRepository.findAll();
    }

    /* 커뮤니티 게시글 상세 조회 */
    @Transactional(readOnly = true)
    public CommunityPost findPost(Integer communityPostId){
        return findCommunityPostByCommunityPostId(communityPostId);
    }

    /* 커뮤니티 게시글 작성 */
    public CommunityPost addPost(CommunityPostCreateDto communityPostCreateDto) {
        CommunityPost communityPost = CommunityPost.builder()
                .title(communityPostCreateDto.getTitle())
                .content(communityPostCreateDto.getContent())
                //.imageUrl(communityPostCreateDto.getImageUrl())
                .build();
        return communityPostsRepository.save(communityPost);
    }

    /* 커뮤니티 게시글 수정 */
    public CommunityPostResponseDto updatePost(Integer communityPostId, CommunityPostUpdateDto communityPostUpdateDto) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        return CommunityPostResponseDto.of(communityPostsRepository.save(communityPost.updateCommunityPost(communityPostUpdateDto)));
    }

    /* 커뮤니티 게시글 삭제 */
    public void deletePost(Integer communityPostId) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        communityPost.updateCommunityPostStatus(CommunityPost.CommunityPostStatus.DELETED);
    }

    /* 게시글 좋아요 달기 */
    // 좋아요 안 눌러져 있으면 좋아요 생성

    /* 게시글 좋아요 취소 */
    // 좋아요 눌러져 있으면 좋아요 삭제
}
