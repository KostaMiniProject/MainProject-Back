package kosta.main.users.auth.oauth2;

import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserOAuth2DTO {

    private String temporaryUserId;
    private String provider;
    private Boolean social;
    private String email;
    private String name;
    private String roles;

    public static UserOAuth2DTO of(User user, String temporaryUserId){
        return new UserOAuth2DTO(
                temporaryUserId,
                user.getProvider(),
                user.getSocial(),
                user.getEmail(),
                user.getName(),
                user.getRoles()
        );
    }
}
