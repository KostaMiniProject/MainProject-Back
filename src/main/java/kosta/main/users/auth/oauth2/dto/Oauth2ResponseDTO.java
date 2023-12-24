package kosta.main.users.auth.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Oauth2ResponseDTO {

    private Integer userId;
    private String userEmail;
    private boolean additionalInfo;
}
