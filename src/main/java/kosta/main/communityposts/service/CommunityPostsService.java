package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.likes.dto.LikeDto;
import kosta.main.likes.entity.Like;
import kosta.main.likes.repository.LikesRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityPostsService {
    private final CommunityPostsRepository communityPostsRepository;
    private final UsersRepository usersRepository;
    private final LikesRepository likesRepository;
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

        List<String> imagePaths = new ArrayList<>(files.stream().map(imageService::resizeToBasicSizeAndUpload).toList());
        // 변경 불가능한 리스트를 반환하는 toList() 메소드
        // toList()로 생성한 리스트를 new ArrayList<>를 이용해 새로운 ArrayList로 변환
        // ArrayList는 필요에 따라 요소를 추가하거나 삭제하는 등의 작업을 할 수 있다.
        communityPostUpdateDto.updateImagePaths(imagePaths);
        communityPost.updateCommunityPost(communityPostUpdateDto);
        CommunityPost save = communityPostsRepository.save(communityPost);
        return CommunityPostResponseDto.of(save);
    }

    /* 커뮤니티 게시글 삭제 */
    public void deletePost(Integer communityPostId, Integer userId) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        User user = findUserByUserId(userId);
        if (!communityPost.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("작성자와 삭제하는 사용자가 일치하지 않습니다.");
        }

        communityPost.updateCommunityPostStatus(CommunityPost.CommunityPostStatus.DELETED);
        communityPostsRepository.save(communityPost);
    }

    /* 커뮤니티 좋아요 토글 */
    public LikeDto toggleLikePost(Integer communityPostId, Integer userId) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        User user = findUserByUserId(userId);
        Like targetLike = null;
        for (Like like : communityPost.getLikePostList()) {
            if (like.getUser().getUserId().equals(user.getUserId())) {
                targetLike = like;
                break;
            }
        }

        if(targetLike != null) {
            communityPost.getLikePostList().remove(targetLike);
            targetLike.setCommunityPost(null);
            likesRepository.delete(targetLike);
            return null;
        } else {
            Like like = new Like();
            like.setCommunityPost(communityPost);
            like.setUser(user);
            communityPost.getLikePostList().add(like);
            communityPostsRepository.save(communityPost);
            return LikeDto.of(like);
        }
    }
}