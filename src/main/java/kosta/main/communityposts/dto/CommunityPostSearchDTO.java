package kosta.main.communityposts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommunityPostSearchDTO {
    private Integer communityPostId;

    private String title;

    private String content;

    public CommunityPostSearchDTO(Integer communityPostId, String title, String content) {
        this.communityPostId = communityPostId;
        this.title = title;
        this.content = content;
    }
}
