package kosta.main.communityposts.dto;


import lombok.Getter;

import java.util.List;


@Getter
public class CommunityPostUpdateDto {
    private String title;
    private String content;
    private List<String> imagePaths;

    public void updateImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
