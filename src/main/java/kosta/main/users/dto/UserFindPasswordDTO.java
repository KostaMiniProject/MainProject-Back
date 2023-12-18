package kosta.main.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserFindPasswordDTO {
  private String email;
  private String name;
  private String phone;
}
