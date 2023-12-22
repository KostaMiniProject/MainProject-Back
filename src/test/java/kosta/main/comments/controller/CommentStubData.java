package kosta.main.comments.controller;

import kosta.main.comments.dto.*;
import kosta.main.comments.entity.Comment;
import kosta.main.communityposts.CommunityPostStubData;
import kosta.main.communityposts.entity.CommunityPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public List<CommentParentDTO> getCommentListDTO() {

        return convert(getParentAndChildComment());
    }

    private List<CommentParentDTO> convert(List<Comment> comments) {
        Map<Integer,CommentParentDTO> result = new HashMap<>();
        for (Comment comment : comments) {
            if(comment.getParent() == null) {
                result.put(comment.getCommentId(),CommentParentDTO.from(comment,1));
            }
            else {
                result.get(comment.getParent().getCommentId()).addChild(CommentChildDTO.from(comment,1));
            }
        }
        return new ArrayList<>(result.values());
    }

    public CommentUpdateDTO getCommentUpdateDTO() {
        return new CommentUpdateDTO(PARENT_COMMENT_CONTENT);
    }
}
