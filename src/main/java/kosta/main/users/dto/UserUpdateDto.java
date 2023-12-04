package kosta.main.users.dto;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UserUpdateDto {
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
}
