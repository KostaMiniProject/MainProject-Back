package kosta.main.comments.controller;

import kosta.main.comments.dto.CommentCreateDTO;
import kosta.main.comments.dto.CommentDTO;
import kosta.main.comments.dto.CommentListDTO;
import kosta.main.comments.dto.CommentUpdateDTO;
import kosta.main.comments.entity.Comment;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.entity.CommunityPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kosta.main.comments.dto.CommentListDTO.convertCommentToDto;

public class CommentStubData {

    public static final int COMMENT_ID = 2;
    public static final int PARENT_COMMENT_ID = 1;
    public static final String PARENT_COMMENT_CONTENT = "부모댓글";
    public static final int CHILD_COMMENT_ID = 2;
    public static final String CHILD_COMMENT_CONTENT = "자식댓글";
    public static final int ANOTHER_PARENT_COMMENT_ID = 3;
    public static final String ANOTHER_PARENT_COMMENT_CONTENT = "새로운 부모 댓글";

    private CommunityPostStubData communityPostStubData;
    public Comment getParentComment() {
        communityPostStubData = new CommunityPostStubData();
        CommunityPost communityPost = communityPostStubData.getCommunityPost();
        return Comment.builder()
                .commentId(PARENT_COMMENT_ID)
                .communityPost(communityPost)
                .user(communityPost.getUser())
                .content(PARENT_COMMENT_CONTENT)
                .build();
    }

    public List<Comment> getParentAndChildComment() {
        Comment parentComment = getParentComment();
        Comment childComment = Comment.builder()
                .commentId(CHILD_COMMENT_ID)
                .communityPost(parentComment.getCommunityPost())
                .parent(parentComment)
                .user(parentComment.getUser())
                .content(CHILD_COMMENT_CONTENT)
                .build();

        parentComment.getChildren().add(childComment);
        List<Comment> comments = new ArrayList<>();
        comments.add(parentComment);
        comments.add(childComment);
        comments.add(getAnotherParentComment());
        return comments;
    }

    public Comment getAnotherParentComment() {
        communityPostStubData = new CommunityPostStubData();
        CommunityPost communityPost = communityPostStubData.getCommunityPost();
        return Comment.builder()
                .commentId(ANOTHER_PARENT_COMMENT_ID)
                .communityPost(communityPost)
                .user(communityPost.getUser())
                .content(ANOTHER_PARENT_COMMENT_CONTENT)
                .build();
    }

    public CommentDTO getCommentDTO() {
        return new CommentDTO(getParentComment());
    }
    public CommentCreateDTO getCommentCreateDTO() {
        return new CommentCreateDTO(PARENT_COMMENT_CONTENT, PARENT_COMMENT_ID);
    }

    public List<CommentListDTO> getCommentListDTO() {

        return convertNestedStructure(getParentAndChildComment());
    }

     List<CommentListDTO> convertNestedStructure(List<Comment> comments) {
        List<CommentListDTO> result = new ArrayList<>();
        Map<Integer, CommentListDTO> map = new HashMap<>();
        comments.forEach(c -> {
            CommentListDTO dto = convertCommentToDto(c);
            map.put(dto.getCommentId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getCommentId()).getChildren().add(dto);
            else result.add(dto);
        });
        return result;
    }

    public CommentUpdateDTO getCommentUpdateDTO() {
        return new CommentUpdateDTO(PARENT_COMMENT_CONTENT);
    }
}
