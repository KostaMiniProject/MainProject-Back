package kosta.main.likes.dto;

import kosta.main.likes.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeDTO {
    private Integer likeId;
    private Integer communityPostId;
    private Integer userId;

    public static LikeDTO of(Like like) {
        return new LikeDTO(
                like.getLikeId(),
                like.getCommunityPost().getCommunityPostId(),
                like.getUser().getUserId()
        );
    }

}
