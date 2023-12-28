package kosta.main.comments.service;

import kosta.main.comments.dto.*;
import kosta.main.comments.entity.Comment;
import kosta.main.comments.repository.CommentsRepository;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.communityposts.service.CommunityPostsService;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.users.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static kosta.main.global.error.exception.CommonErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentsService {

    private final CommunityPostsService communityPostsService;
    private final CommentsRepository commentsRepository;

    // 댓글 조회(대댓글 할때는 사용자 아이디 필요)
    @Transactional(readOnly = true)
    public List<CommentParentDTO>findCommentsByPostId(Integer communityPostId,User user) {
        communityPostsService.findCommunityPostByCommunityPostId(communityPostId); // 커뮤니티 게시글이 존재하는지 확인
        List<Comment> comments = commentsRepository.findComments(communityPostId);
        return convert(comments,user);
    }

    private List<CommentParentDTO> convert(List<Comment> comments,User user) {
        Map<Integer,CommentParentDTO> result = new HashMap<>();
        Integer userId = user.getUserId();
        for (Comment comment : comments) {
            if(comment.getParent() == null) {
                    result.put(comment.getCommentId(), CommentParentDTO.from(comment, userId));
            }
            else {
                result.get(comment.getParent().getCommentId()).addChild(CommentChildDTO.from(comment,userId));
            }
        }
        return new ArrayList<>(result.values());
    }

    // 댓글 작성(대댓글 할때는 parentId 사용)
    // 댓글 수정

    public CommentDTO updateComment(User user, Integer commentId, CommentUpdateDTO commentUpdateDTO) {
        Comment comment = findCommentByCommentId(commentId);

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("댓글 수정 권한이 없습니다.");
        }

        comment.updateComment(commentUpdateDTO);
        Comment save = commentsRepository.save(comment);
        return new CommentDTO(save);
    }
    // 댓글 삭제

    public void deleteComment(Integer commentId, User user) {
        Comment comment = findCommentByCommentId(commentId);

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }

        comment.updateCommentStatus(Comment.CommentStatus.DELETED);
        commentsRepository.save(comment);
    }
    public Comment findCommentByCommentId(Integer commentId) {
        return commentsRepository.findById(commentId).orElseThrow(() -> new BusinessException(COMMENT_NOT_FOUND));
    }

    public CommentDTO addReply(User user, Integer communityPostId, CommentCreateDTO commentCreateDTO) {

        CommunityPost communityPost = communityPostsService.findCommunityPostByCommunityPostId(communityPostId);
        Comment commentByCommentId = findCommentByCommentId(commentCreateDTO.getParentId());

        if(!Objects.equals(commentByCommentId.getCommunityPost().getCommunityPostId(), communityPostId))
            throw new BusinessException(NOT_EQUAL_COMMUNITY_POST);

        Comment comment = Comment.builder()
                .user(user)
                .content(commentCreateDTO.getContent())
                .communityPost(communityPost)
                .parent(commentByCommentId)
                .build();

        commentByCommentId.updateChild(comment);
        commentsRepository.save(comment);
        commentsRepository.save(commentByCommentId);
        return new CommentDTO(comment);
    }

    public CommentDTO addComment(User user, Integer communityPostId, CommentCreateDTO commentCreateDTO) {
        //부모 아이디가 존재할 경우 답글 달기 로직으로 변경
        if(commentCreateDTO.getParentId() != null) {
            return addReply(user,communityPostId,commentCreateDTO);
        }
        if (user == null) throw new BusinessException(USER_NOT_FOUND);
        CommunityPost communityPost = communityPostsService.findCommunityPostByCommunityPostId(communityPostId);

        Comment comment = Comment.builder()
                .user(user)
                .content(commentCreateDTO.getContent())
                .communityPost(communityPost)
                .build();
        return new CommentDTO(commentsRepository.save(comment));
    }
}
