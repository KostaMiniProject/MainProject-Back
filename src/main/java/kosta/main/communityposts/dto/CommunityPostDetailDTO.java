package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommunityPostDetailDTO {
    private Integer communityPostId;

    private Boolean postOwner;

    private String title;
    private String content;

    private Integer views;
    private CommunityPost.CommunityPostStatus communityPostStatus;

    private Integer likeCount;
}
