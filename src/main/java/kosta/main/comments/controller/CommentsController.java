package kosta.main.comments.controller;

import kosta.main.comments.dto.CommentCreateDTO;
import kosta.main.comments.dto.CommentDTO;
import kosta.main.comments.dto.CommentListDTO;
import kosta.main.comments.dto.CommentUpdateDTO;
import kosta.main.comments.service.CommentsService;
import kosta.main.users.entity.LoginUser;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community-posts")
public class CommentsController {


    private final CommentsService commentsService;
    /* 커뮤니티 댓글 조회 */
    @GetMapping("/{communityPostId}/comments")
    public ResponseEntity<List<CommentListDTO>> findComments(@PathVariable("communityPostId") Integer communityPostId) {
        return ResponseEntity.ok(commentsService.findCommentsByPostId(communityPostId));
    }

    /* 커뮤니티 댓글 작성 */
    @PostMapping("/{communityPostId}/comments")
    public ResponseEntity<CommentDTO> addComment(@LoginUser User user,
                                                 @PathVariable("communityPostId") Integer communityPostId,
                                                 @RequestBody CommentCreateDTO commentCreateDTO) {
        CommentDTO commentDTO = commentsService.addComment(user, communityPostId, commentCreateDTO);
        return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
    }


    /* 커뮤니티 댓글 수정 */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@LoginUser User user,
                                                    @PathVariable("commentId") Integer commentId,
                                                    @RequestBody CommentUpdateDTO commentUpdateDTO) {
        return new ResponseEntity<>(commentsService.updateComment(user, commentId, commentUpdateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Integer commentId,
                                           @LoginUser User user) {
        commentsService.deleteComment(commentId, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
