package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommunityPostSelectDto { // stackoverflow에러로 인하여 조회 Dto 처리
    private Integer communityPostId;

    private User user;

    private String title;

    private String content;

    private Integer views;
    private CommunityPost.CommunityPostStatus communityPostStatus;

    //private String imageUrl;

    private Integer likeCount;

    public static CommunityPostSelectDto from (CommunityPost communityPost){ //from : 엔티티를 DTO로 생성
        return new CommunityPostSelectDto (
                communityPost.getCommunityPostId(),
                communityPost.getUser(),
                communityPost.getTitle(),
                communityPost.getContent(),
                communityPost.getViews(),
                communityPost.getCommunityPostStatus(),
                //communityPost.getImageUrl(),
                communityPost.getLikePostList().size()
        );
    }
}
