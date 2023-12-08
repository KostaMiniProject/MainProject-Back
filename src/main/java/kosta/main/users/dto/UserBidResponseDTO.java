package kosta.main.users.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserBidResponseDTO {

    private Integer userId;
    private String name;

    private String address;

    private String imageUrl;


    private Integer rating;

    public static UserBidResponseDTO from(User user){
        return UserBidResponseDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .address(user.getAddress())
                .imageUrl(user.getProfileImage())
                .rating(user.getRating())
                .build();
    }
}
