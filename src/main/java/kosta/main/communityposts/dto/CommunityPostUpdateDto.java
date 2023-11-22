package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import lombok.Getter;

@Getter
public class CommunityPostUpdateDto {
    private String title;
    private String content;
    private CommunityPost.CommunityPostStatus communityPostStatus;
    private String imageUrl;
}
