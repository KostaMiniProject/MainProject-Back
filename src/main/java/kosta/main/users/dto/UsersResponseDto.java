package kosta.main.users.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersResponseDto {

    private String email;

    private String name;

    private String address;

    private String phone;

//    private String profileImage;

    private User.UserStatus userStatus;

    public static UsersResponseDto of(User user){
        return new UsersResponseDto(
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getPhone(),
                //user.getProfileImage(),
                user.getUserStatus()
        );
    }

}
