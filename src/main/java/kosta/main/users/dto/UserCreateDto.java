package kosta.main.users.dto;


import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserCreateDto {

    private String email;

    private String password;

    private String name;

    private String address;

    private String phone;

    private String profileImage;


}
