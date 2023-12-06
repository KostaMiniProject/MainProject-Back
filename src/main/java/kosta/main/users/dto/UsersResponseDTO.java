package kosta.main.users.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersResponseDTO {

    private String email;

    private String name;

    private String address;

    private String phone;

    private String profileImage;

    private User.UserStatus userStatus;

    public static UsersResponseDTO of(User user){
        return new UsersResponseDTO(
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getPhone(),
                user.getProfileImage(),
                user.getUserStatus()
        );
    }

}
