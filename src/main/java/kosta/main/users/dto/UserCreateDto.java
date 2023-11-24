package kosta.main.users.dto;


import kosta.main.users.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserCreateDto {

    private String email;

    private String password;

    private String name;

    private String address;

    private String phone;

    private String profileImage;

    private User.UserStatus userStatus;

}
