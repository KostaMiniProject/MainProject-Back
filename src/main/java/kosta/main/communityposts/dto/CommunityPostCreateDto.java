package kosta.main.communityposts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommunityPostCreateDto {
    private String title;
    private Integer userId;
    private String content;

}
