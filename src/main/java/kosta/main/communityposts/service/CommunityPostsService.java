package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.CommunityPostCreateDto;
import kosta.main.communityposts.dto.CommunityPostResponseDto;
import kosta.main.communityposts.dto.CommunityPostSelectDto;
import kosta.main.communityposts.dto.CommunityPostUpdateDto;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.likes.entity.Like;
import kosta.main.likes.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityPostsService {
    private final LikesRepository likesRepository;
    private final CommunityPostsRepository communityPostsRepository;
    /* RuntimeException 추상 메소드 */
    public CommunityPost findCommunityPostByCommunityPostId(Integer communityPostId) {
        return communityPostsRepository.findById(communityPostId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    /* 커뮤니티 목록 조회 */
    @Transactional(readOnly = true)
    public List<CommunityPostSelectDto> findPosts() {
        List<CommunityPost> posts = communityPostsRepository.findAll();
        return posts.stream()
                .map(CommunityPostSelectDto::from)
                .collect(Collectors.toList());
    }

    /* 커뮤니티 게시글 상세 조회 */
    @Transactional(readOnly = true)
    public CommunityPostSelectDto findPost(Integer communityPostId){
        CommunityPost post = findCommunityPostByCommunityPostId(communityPostId);
        return CommunityPostSelectDto.from(post);
    }

    /* 커뮤니티 게시글 작성 */
    public CommunityPost addPost(CommunityPostCreateDto communityPostCreateDto) {
        CommunityPost communityPost = CommunityPost.builder()
                .title(communityPostCreateDto.getTitle())
                .content(communityPostCreateDto.getContent())
                .imageUrl(communityPostCreateDto.getImageUrl())
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
    
    /* 커뮤니티 좋아요 */
    public void likePost(Integer communityPostId) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        Optional<Like> found = likesRepository.findByCommunityPost(communityPost);
        if (found.isEmpty()) {
            communityPost.likePostUp();
            Like like = Like.of(communityPost);
            likesRepository.save(like);
        } else {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }
    }
    
    /* 커뮤니티 좋아요 취소 */
    public void disLikePost(Integer communityPostId) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        Optional<Like> found = likesRepository.findByCommunityPost(communityPost);
        if (found.isPresent()) {
            communityPost.likePostDown();
            likesRepository.delete(found.get());
            likesRepository.flush();
        } else {
            throw new RuntimeException("이미 좋아요를 취소하였습니다.");
        }
    }
}
