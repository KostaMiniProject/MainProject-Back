package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommunityPostDetailDto {
    private Integer communityPostId;

    private Integer userId;

    private String title;
    private String content;

    private Integer views;
    private CommunityPost.CommunityPostStatus communityPostStatus;

    private Integer likeCount;

    public static CommunityPostDetailDto from (CommunityPost communityPost){ //from : 엔티티를 DTO로 생성
        return new CommunityPostDetailDto(
                communityPost.getCommunityPostId(),
                communityPost.getUser().getUserId(),
                communityPost.getTitle(),
                communityPost.getContent(),
                communityPost.getViews(),
                communityPost.getCommunityPostStatus(),
                communityPost.getLikePostList().size()
        );
    }
}
