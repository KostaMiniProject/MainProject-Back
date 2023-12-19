package kosta.main.communityposts.dto;

import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.dto.response.UsersResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommunityPostResponseDTO {
    private UsersResponseDTO user;

    private String title;

    private String content;

    private Integer views;
    private CommunityPost.CommunityPostStatus communityPostStatus;
    private Integer likeCount;

    public static CommunityPostResponseDTO of(CommunityPost communityPost){ //of : 데이터를 가지고 새로운 객체를 만들때 사용
        return new CommunityPostResponseDTO(
                UsersResponseDTO.of(communityPost.getUser()),
                communityPost.getTitle(),
                communityPost.getContent(),
                communityPost.getViews(),
                communityPost.getCommunityPostStatus(),
                communityPost.getLikePostList().size()
        );
    }
}
