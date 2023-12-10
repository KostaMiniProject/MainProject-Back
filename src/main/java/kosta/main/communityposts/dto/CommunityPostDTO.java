package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityPostDTO {
    private Integer communityPostId;
    private String title;
    private String content;
    private Integer views;
    private List<String> imagePaths;

    public CommunityPostDTO(CommunityPost communityPost) {
        this.communityPostId = communityPost.getCommunityPostId();
        this.title = communityPost.getTitle();
        this.content = communityPost.getContent();
        this.views = communityPost.getViews();
        this.imagePaths = communityPost.getImages();
    }
}
