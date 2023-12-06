package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommunityPostListDTO { // stackoverflow에러로 인하여 조회 Dto 처리
    private Integer communityPostId;

    private String title;

    private String content;

    private Integer views;
    private CommunityPost.CommunityPostStatus communityPostStatus;

    private Integer likeCount;

    public static CommunityPostListDTO from (CommunityPost communityPost){ //from : 엔티티를 DTO로 생성
        return new CommunityPostListDTO(
                communityPost.getCommunityPostId(),
                communityPost.getTitle(),
                communityPost.getContent(),
                communityPost.getViews(),
                communityPost.getCommunityPostStatus(),
                communityPost.getLikePostList().size()
        );
    }


}