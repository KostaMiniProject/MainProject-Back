package kosta.main.likes.dto;

import kosta.main.likes.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeDto {
    private Integer id;
    private Integer communityPostId;
    private Integer userId;

    public static LikeDto of(Like like) {
        return new LikeDto(
                like.getLikeId(),
                like.getCommunityPost().getCommunityPostId(),
                like.getUser().getUserId()
        );
    }

}
