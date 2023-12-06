package kosta.main.users.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserCreateDTO {

    private String email;

    private String password;
    private String checkPassword;

    private String name;

    private String address;

    private String phone;

    public void updatePassword(String password) {
        this.password = password;
    }
}
