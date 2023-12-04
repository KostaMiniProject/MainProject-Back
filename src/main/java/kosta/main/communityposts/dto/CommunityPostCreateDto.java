package kosta.main.communityposts.dto;

import lombok.Getter;

@Getter
public class CommunityPostCreateDto {
    private String title;
    private Integer userId;
    private String content;
}
