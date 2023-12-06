package kosta.main.communityposts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityPostUpdateDTO {
    private String title;
    private String content;
    private List<String> imagePaths;

    public void updateImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
