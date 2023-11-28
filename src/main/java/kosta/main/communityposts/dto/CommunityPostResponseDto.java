package kosta.main.communityposts.dto;

import jakarta.persistence.*;
import kosta.main.communityposts.entity.CommunityPost;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommunityPostResponseDto {
    private User user;

    private String title;

    private String content;

    private Integer views;
    private CommunityPost.CommunityPostStatus communityPostStatus;

    //private String imageUrl;

    public static CommunityPostResponseDto of(CommunityPost communityPost){ //of : 데이터를 가지고 새로운 객체를 만들때 사용
        return new CommunityPostResponseDto(
                communityPost.getUser(),
                communityPost.getTitle(),
                communityPost.getContent(),
                communityPost.getViews(),
                communityPost.getCommunityPostStatus()
                //communityPost.getImageUrl()
        );
    }
}
