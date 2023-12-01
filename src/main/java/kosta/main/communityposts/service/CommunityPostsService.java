package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.likes.dto.LikeDto;
import kosta.main.likes.entity.Like;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityPostsService {
    private final CommunityPostsRepository communityPostsRepository;
    private final UsersRepository usersRepository;


    private final ImageService imageService;
    /* RuntimeException 추상 메소드 */

    public CommunityPost findCommunityPostByCommunityPostId(Integer communityPostId) {
        return communityPostsRepository.findById(communityPostId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    private User findUserByUserId(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }

    /* 커뮤니티 목록 조회 */
    @Transactional(readOnly = true)
    public List<CommunityPostListDto> findPosts() {
        List<CommunityPost> posts = communityPostsRepository.findAll();
        return posts.stream()
                .map(CommunityPostListDto::from)
                .collect(Collectors.toList());
    }

    /* 커뮤니티 게시글 상세 조회 */
    @Transactional(readOnly = true)
    public CommunityPostDetailDto findPost(Integer communityPostId){
        CommunityPost post = findCommunityPostByCommunityPostId(communityPostId);
        return CommunityPostDetailDto.from(post);
    }

    /* 커뮤니티 게시글 작성 */
    public CommunityPost addPost(CommunityPostCreateDto communityPostCreateDto, List<MultipartFile> files) {
        List<String> imagePaths = files.stream().map(imageService::resizeToBasicSizeAndUpload).toList();
        User user = findUserByUserId(communityPostCreateDto.getUserId());
        CommunityPost communityPost = CommunityPost.builder()
                .user(user)
                .title(communityPostCreateDto.getTitle())
                .content(communityPostCreateDto.getContent())
                .images(imagePaths)
                .build();
        return communityPostsRepository.save(communityPost);
    }

    /* 커뮤니티 게시글 수정 */
    public CommunityPostResponseDto updatePost(Integer communityPostId, CommunityPostUpdateDto communityPostUpdateDto, List<MultipartFile> files) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);

        if (!communityPost.getUser().getUserId().equals(communityPostUpdateDto.getUserId())) {
            throw new RuntimeException("작성자와 수정하는 사용자가 일치하지 않습니다.");
        }

        List<String> imagePaths = files.stream().map(imageService::resizeToBasicSizeAndUpload).toList();
        communityPostUpdateDto.updateImagePaths(imagePaths);
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