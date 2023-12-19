package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommunityPostDetailDTO {
    private Integer communityPostId;

    private Boolean postOwner;

    private String title;
    private String content;

    private Integer views;
    private List<String> imageUrl;
    private CommunityPost.CommunityPostStatus communityPostStatus;

    private Integer likeCount;

    public static CommunityPostDetailDTO from(CommunityPost communityPost,boolean postOwner){
        return CommunityPostDetailDTO.builder()
                .communityPostId(communityPost.getCommunityPostId())
                .postOwner(postOwner)
                .title(communityPost.getTitle())
                .content(communityPost.getContent())
                .views(communityPost.getViews())
                .imageUrl(communityPost.getImages())
                .communityPostStatus(communityPost.getCommunityPostStatus())
                .likeCount(communityPost.getLikePostList().size())
                .build();
    }
}
