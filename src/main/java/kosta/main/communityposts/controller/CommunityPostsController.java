package kosta.main.communityposts.controller;


import kosta.main.communityposts.dto.*;
import kosta.main.communityposts.service.CommunityPostsService;
import kosta.main.global.dto.PageInfo;
import kosta.main.global.dto.PageResponseDto;
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

import java.util.ArrayList;
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
    public ResponseEntity<?> findPosts(@PageableDefault(page = 0, size = 10, sort = "communityPostId", direction = Sort.Direction.DESC) Pageable pageable,@LoginUser User user) {
        Page<CommunityPostDetailDTO> posts = communityPostsService.findPosts(pageable, user);
        List<CommunityPostDetailDTO> list = posts.stream().toList();

        return new ResponseEntity<>(new PageResponseDto<>(list, PageInfo.of(posts)), HttpStatus.OK);
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
    public ResponseEntity<CommunityPostDTO> addPost(@LoginUser User user, @RequestPart("communityPostCreateDTO") CommunityPostCreateDTO communityPostCreateDTO,
                                                    @RequestPart(value = "file",required = false) List<MultipartFile> files) {
        if(files == null) files = new ArrayList<>();
        CommunityPostDTO communityPostDTO = communityPostsService.addPost(user, communityPostCreateDTO,files);
        return ResponseEntity.ok(communityPostDTO);
    }

    /* 커뮤니티 게시글 수정 */
    @PutMapping("/{communityPostId}")
    public ResponseEntity<CommunityPostResponseDTO> updatePost(@LoginUser User user,
                                                               @PathVariable("communityPostId") Integer communityPostId,
                                                               @RequestPart("communityPostUpdateDTO") CommunityPostUpdateDTO communityPostUpdateDTO,
                                                               @RequestPart("file") List<MultipartFile> files){
        return new ResponseEntity<>(communityPostsService.updatePost(user, communityPostId, communityPostUpdateDTO,files), HttpStatus.OK);
    }

    /* 커뮤니티 게시글 삭제 */
    @DeleteMapping("/{communityPostId}")
    public ResponseEntity<?> deletePost(@PathVariable("communityPostId") Integer communityPostId,
                                        @LoginUser User user) {
        communityPostsService.deletePost(communityPostId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /* 커뮤니티 좋아요 */
    @PutMapping("/likes/{communityPostId}")
    public ResponseEntity<?> toggleLikePost(@LoginUser User user,
                                            @PathVariable("communityPostId") Integer communityPostId) {
        Object response = communityPostsService.toggleLikePost(communityPostId, user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /* 커뮤니티 게시글 검색 */
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword,
                                                         @PageableDefault(page = 0, size = 10) Pageable pageable,
                                                         @LoginUser User user) {
        Page<CommunityPostListDTO> search = communityPostsService.search(keyword, pageable, user);
        List<CommunityPostListDTO> list = search.stream().toList();
        return new ResponseEntity<>(new PageResponseDto<>(list, PageInfo.of(search)), HttpStatus.OK);
    }
}

