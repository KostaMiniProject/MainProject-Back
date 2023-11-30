package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.CommunityPostCreateDto;
import kosta.main.communityposts.dto.CommunityPostResponseDto;
import kosta.main.communityposts.dto.CommunityPostSelectDto;
import kosta.main.communityposts.dto.CommunityPostUpdateDto;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.likes.dto.LikeDto;
import kosta.main.likes.entity.Like;
import kosta.main.likes.repository.LikesRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityPostsService {
    private final LikesRepository likesRepository;
    private final CommunityPostsRepository communityPostsRepository;
    private final UsersRepository usersRepository;

    /* RuntimeException 메소드 */
    public CommunityPost findCommunityPostByCommunityPostId(Integer communityPostId) {
        return communityPostsRepository.findById(communityPostId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    private User findUserByUserId(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
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
    
    /* 커뮤니티 좋아요 */
    public LikeDto likePost(Integer communityPostId, Integer userId) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        User user = findUserByUserId(userId);
        Like like = new Like();
        like.setCommunityPost(communityPost);
        like.setUser(user);
        communityPost.getLikePostList().add(like);
        return LikeDto.of(like);
    }
    
    /* 커뮤니티 좋아요 취소 */
    public void disLikePost(Integer communityPostId, Integer userId) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        User user = findUserByUserId(userId);
        List<Like> likes = communityPost.getLikePostList();
        likes.removeIf(like -> like.getUser().equals(user));
    }
}