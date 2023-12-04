package kosta.main.communityposts.controller;


import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.service.CommunityPostsService;
import kosta.main.likes.dto.LikeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community-posts")
public class CommunityPostsController {
    private final CommunityPostsService communityPostsService;

    /* 커뮤니티 목록 조회 */
    /* 테스트 성공 확인 */
    @GetMapping
    public List<CommunityPostListDto> findPosts() {
        return communityPostsService.findPosts();
    }

    /* 커뮤니티 게시글 상세 조회 */
    /* 테스트 성공 확인 */
    @GetMapping("/{communityPostId}")
    public ResponseEntity<CommunityPostDetailDto> findPost(@PathVariable("communityPostId") Integer communityPostId) throws Exception {
        CommunityPostDetailDto communityPost = communityPostsService.findPost(communityPostId);
        return ResponseEntity.ok(communityPost);
    }

    /* 커뮤니티 게시글 작성 */
    /* 테스트 성공 확인 */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommunityPost> addPost(@RequestPart("communityPostCreateDto") CommunityPostCreateDto communityPostCreateDto,
                                                 @RequestPart("file") List<MultipartFile> files) {
        CommunityPost communityPost = communityPostsService.addPost(communityPostCreateDto,files);
        return ResponseEntity.ok(communityPost);
    }

    /* 커뮤니티 게시글 수정 */
    @PutMapping("/{communityPostId}")
    public ResponseEntity<CommunityPostResponseDto> updatePost(@PathVariable("communityPostId") Integer communityPostId,
                                                               @RequestPart("communityPostUpdateDto") CommunityPostUpdateDto communityPostUpdateDto,
                                                               @RequestPart("file") List<MultipartFile> files){
        return new ResponseEntity<>(communityPostsService.updatePost(communityPostId, communityPostUpdateDto,files), HttpStatus.OK);
    }

    /* 커뮤니티 게시글 삭제 */
    @DeleteMapping("/{communityPostId}")
    public ResponseEntity<?> deletePost(@PathVariable("communityPostId") Integer communityPostId,
                                           @RequestParam("userId") Integer userId) {
        communityPostsService.deletePost(communityPostId, userId);
        return ResponseEntity.ok().build();
    }

    /* 커뮤니티 좋아요 */
    @PutMapping("/likes/{communityPostId}")
    public ResponseEntity<?> toggleLikePost(@PathVariable("communityPostId") Integer communityPostId, @RequestParam Integer userId) {
        LikeDto likeDto = communityPostsService.toggleLikePost(communityPostId, userId);
        return ResponseEntity.ok(likeDto);
    }
}

