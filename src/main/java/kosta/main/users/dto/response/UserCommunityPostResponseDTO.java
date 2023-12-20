package kosta.main.users.dto.response;

import kosta.main.communityposts.dto.CommunityPostListDTO;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.exchangeposts.entity.ExchangePost;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Getter
@Builder
public class UserCommunityPostResponseDTO {
    private Integer communityPostId;

    private String title;

    private String content;

    private Integer views;
    private List<String> imageUrl;
    private CommunityPost.CommunityPostStatus communityPostStatus;

    private Integer likeCount;

    public static UserCommunityPostResponseDTO from (CommunityPost communityPost){ //from : 엔티티를 DTO로 생성
        return new UserCommunityPostResponseDTO(
            communityPost.getCommunityPostId(),
            communityPost.getTitle(),
            communityPost.getContent(),
            communityPost.getViews(),
            communityPost.getImages(),
            communityPost.getCommunityPostStatus(),
            communityPost.getLikePostList().size()
        );
    }

}
