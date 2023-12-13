package kosta.main.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {
  // 1. @기호를 포함해야 한다.
  // 2. @기호를 기준으로 이메일 주소를 이루는 로컬호스트와 도메인으로 구분
  // 3. 도메인은 최소 1개 이상의 .기호와 최소한 2개 이상의 알파벳으로 구성
  @Email
  @NotEmpty(message = "이메일을 입력해 주세요")
  private String email;
}