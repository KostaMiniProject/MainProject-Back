package kosta.main.users.dto.response;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserItemResponseDTO {

    private Integer userId;
    private String email;

    private String name;

    private String address;

    private String phone;
    private Double rating;
    private String profileImage;

    public static UserItemResponseDTO of(User user){
        return new UserItemResponseDTO(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getPhone(),
                user.getRating(),
                user.getProfileImage()
        );
    }
    public static UserItemResponseDTO deletedOf(){
        return new UserItemResponseDTO(
                0,
                "deleted",
                "삭제된 유저",
                "삭제된 주소",
                "삭제된 전화번호",
                0.0,
                "https://d30zoz4y43tmi6.cloudfront.net/simpleProfile.jpg"
        );
    }
}
