package kosta.main.communityposts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CommunityPostUpdateDto {
    private String title;
    private String content;
    private String imageUrl;

}
