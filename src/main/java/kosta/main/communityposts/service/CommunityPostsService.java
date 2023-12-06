package kosta.main.communityposts.service;

import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.repository.CommunityPostsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.s3upload.service.ImageService;
import kosta.main.likes.dto.LikeDto;
import kosta.main.likes.entity.Like;
import kosta.main.likes.repository.LikesRepository;
import kosta.main.users.entity.User;
import kosta.main.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static kosta.main.global.error.exception.ErrorCode.*;

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
        return communityPostsRepository.findById(communityPostId).orElseThrow(() -> new BusinessException(COMMUNITY_POST_NOT_FOUND));
    }

    private User findUserByUserId(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }

    /* 커뮤니티 목록 조회 */
    @Transactional(readOnly = true)
    public Page<CommunityPostListDto> findPosts(Pageable pageable) {
        Page<CommunityPost> posts = communityPostsRepository.findAll(pageable);
        return posts.map(CommunityPostListDto::from);
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
            throw new BusinessException(NOT_COMMUNITY_POST_OWNER);
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
    public void deletePost(Integer communityPostId, User user) {
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        if (!communityPost.getUser().getUserId().equals(user.getUserId())) {
            throw new BusinessException(NOT_COMMUNITY_POST_OWNER);
        }

        communityPost.updateCommunityPostStatus(CommunityPost.CommunityPostStatus.DELETED);
        communityPostsRepository.save(communityPost);
    }

    /* 커뮤니티 좋아요 토글 */
    public Object toggleLikePost(Integer communityPostId, Integer userId) { // 좋아요 누르면 likeDto/ 취소를 누르면 CommunityPostLikeCancelledDto를 반환해야 하므로 Object타입
        CommunityPost communityPost = findCommunityPostByCommunityPostId(communityPostId);
        User user = findUserByUserId(userId);

        // 좋아요를 누른 유저가 로그인한 유저와 일치한지의 로직
        Like targetLike = null; // 좋아요 여부를 결정해주는 변수 targetLike 생성
        for (Like like : communityPost.getLikePostList()) { // 엔티티에서 가져온 좋아요 목록을 like에 담아 향상된 for문으로 순회
            if (like.getUser().getUserId().equals(user.getUserId())) { // 좋아요를 한 유저의 유저아이디와 현재 로그인한 유저아이디가 일치한지
                targetLike = like; // 일치하면 like를 targetLike에 담음
                break; // 일치하여 targetLike에 좋아요 목록이 담겼으니 for문에서 빠져나온다.
            }
        }

        // 좋아요와 좋아요 취소 로직
        if(targetLike != null) { // 좋아요 목록에 좋아요가 있다면
            communityPost.getLikePostList().remove(targetLike); // 엔티티의 좋아요 목록에서 targetLike를 제거하여 like와 communityPost의 연관관계를 제거한다.
            targetLike.setCommunityPost(null); // targetLike가 CommunityPost를 더 이상 참조하지 않도록 설정하여 CommunityPost 엔티티에서 Like 엔티티를 제거한다.
            likesRepository.delete(targetLike); // targetLike 객체를 데이터베이스에서 제거한다.
            // 이전 두 단계에서 '좋아요'의 상태를 업데이트했으므로, 이제 안전하게 DB에서 '좋아요'를 제거하는 로직이다.
            return new CommunityPostLikeCancelledDto("좋아요 취소 했습니다."); //retrurn null이였으나 NullPointerException 발생 가능성이 있어서 수정하여 취소 메세지 반환
        } else { // 좋아요 목록에 좋아요가 없다면
            Like like = new Like(); // 새로운 like 객체를 생성한다.
            like.setCommunityPost(communityPost); // 좋아요를 누른 게시글을 like 객체에 연결
            like.setUser(user); // 좋아요를 누른 사용자를 like 객체에 연결
            communityPost.getLikePostList().add(like); // 좋아요 목록에 like 객체 추가
            communityPostsRepository.save(communityPost); // 좋아요가 추가된 게시글을 DB에 저장
            return LikeDto.of(like); // like 객체를 LikeDto로 변환하여 반환
        }
    }
}