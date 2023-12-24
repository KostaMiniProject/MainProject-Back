package kosta.main.users.auth.oauth2.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class KakaoDTO {

    private String accessToken;
    private String refreshToken;
    private String idToken;

}
