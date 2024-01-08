package kosta.main.users.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFindPasswordDTO {
  private String email;
  private String name;
  private String phone;

  public static UserFindPasswordDTO from(String email, String name, String phone){
    return new UserFindPasswordDTO(email, name, phone);
  }
}
