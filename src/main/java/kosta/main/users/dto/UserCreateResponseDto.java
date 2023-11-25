package kosta.main.users.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateResponseDto {

    private String email;

    private String name;

    private String address;

    private String phone;


    public static UserCreateResponseDto of(User user){
        return new UserCreateResponseDto(
                user.getEmail(),
                user.getName(),
                user.getAddress(),
                user.getPhone()
        );
    }

}
