package kosta.main.users.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateDto {
    private String password;

    private String name;

    private String address;

    private String phone;

    private String profileImage;

    private User.UserStatus userStatus;
}
