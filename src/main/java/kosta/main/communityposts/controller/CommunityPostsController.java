package kosta.main.communityposts.controller;


import kosta.main.comments.dto.CommentCreateDTO;
import kosta.main.comments.dto.CommentDTO;
import kosta.main.comments.dto.CommentListDTO;
import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.service.CommunityPostsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public Page<CommunityPostListDTO> findPosts(@PageableDefault(page = 0, size = 10, sort = "communityPostId", direction = Sort.Direction.DESC) Pageable pageable) {
        return communityPostsService.findPosts(pageable);
    }

    /* 커뮤니티 게시글 상세 조회 */
    /* 테스트 성공 확인 */
    @GetMapping("/{communityPostId}")
    public ResponseEntity<CommunityPostDetailDTO> findPost(@LoginUser User user, @PathVariable("communityPostId") Integer communityPostId) {
        CommunityPostDetailDTO communityPost = communityPostsService.findPost(user, communityPostId);
        return ResponseEntity.ok(communityPost);
    }

    /* 커뮤니티 게시글 작성 */
    /* 테스트 성공 확인 */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommunityPostDTO> addPost(@LoginUser User user, @RequestPart("communityPostCreateDto") CommunityPostCreateDTO communityPostCreateDto,
                                                    @RequestPart("file") List<MultipartFile> files) {
        CommunityPostDTO communityPostDto = communityPostsService.addPost(user, communityPostCreateDto,files);
        return ResponseEntity.ok(communityPostDto);
    }

    /* 커뮤니티 게시글 수정 */
    @PutMapping("/{communityPostId}")
    public ResponseEntity<CommunityPostResponseDTO> updatePost(@LoginUser User user,
                                                               @PathVariable("communityPostId") Integer communityPostId,
                                                               @RequestPart("communityPostUpdateDto") CommunityPostUpdateDTO communityPostUpdateDto,
                                                               @RequestPart("file") List<MultipartFile> files){
        return new ResponseEntity<>(communityPostsService.updatePost(user, communityPostId, communityPostUpdateDto,files), HttpStatus.OK);
    }

    /* 커뮤니티 게시글 삭제 */
    @DeleteMapping("/{communityPostId}")
    public ResponseEntity<?> deletePost(@PathVariable("communityPostId") Integer communityPostId,
                                        @LoginUser User user) {
        communityPostsService.deletePost(communityPostId, user);
        return ResponseEntity.ok().build();
    }

    /* 커뮤니티 좋아요 */
    @PutMapping("/likes/{communityPostId}")
    public ResponseEntity<?> toggleLikePost(@LoginUser User user,
                                            @PathVariable("communityPostId") Integer communityPostId) {
        Object response = communityPostsService.toggleLikePost(communityPostId, user);
        return ResponseEntity.ok(response);
    }

    /* 커뮤니티 댓글 조회 */
    @GetMapping("/{communityPostId}/comments")
    public ResponseEntity<List<CommentListDTO>> findComments(@PathVariable("communityPostId") Integer communityPostId) {
        return ResponseEntity.ok(communityPostsService.findCommentsByPostId(communityPostId));
    }

    @PostMapping("/{communityPostId}/comments")
    public ResponseEntity<CommentDTO> addComment(@LoginUser User user,
                                                 @PathVariable("communityPostId") Integer communityPostId,
                                                 @RequestPart("commentCreateDto") CommentCreateDTO commentCreateDto) {
        CommentDTO commentDTO = communityPostsService.addComment(user, communityPostId, commentCreateDto);
        return ResponseEntity.ok(commentDTO);
    }
}

