package kosta.main.users.dto.response;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersResponseDTO {
    private Integer userId;

    private Double rating;

    private String email;

    private String name;

    private String address;

    private String phone;

    private String profileImage;

    private User.UserStatus userStatus;


    public static UsersResponseDTO of(User user){
        return new UsersResponseDTO(
                user.getUserId(),
                user.getRating(),
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getPhone(),
                user.getProfileImage(),
                user.getUserStatus()
        );
    }

}
