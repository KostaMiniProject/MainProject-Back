package kosta.main.communityposts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CommunityPostUpdateDto {
    private String title;
    private String content;
    private Integer userId;
    private List<String> imagePaths;

    public void updateImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
