package kosta.main.users.auth.oauth2.dto;

import kosta.main.users.dto.request.AddressDTO;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OauthSignUpDTO {

    private String email;

    private String nickName;

    private AddressDTO address;
    private String addressDetail;

    //  # 조건 : 숫자와 하이픈(-)만 작성가능
//  @Pattern(regexp = "^[0-9-]+$", message = "숫자와 하이픈(-)만 입력해주세요.")
    private String phone;


}
