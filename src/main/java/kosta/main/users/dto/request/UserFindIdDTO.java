package kosta.main.users.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserFindIdDTO {
  private String name;
  private String phone;
}
