package kosta.main.users.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateDTO {
    private String password;

    private String checkPassword;

    private String name;

    private String address;

    private String phone;

    private String profileImage;

    private User.UserStatus userStatus;

    public void updateProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
    public void updatePassword(String encodePassword){
        this.password = encodePassword;
    }
}
