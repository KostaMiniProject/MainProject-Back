package kosta.main.users.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserCreateDTO {

  //    # 조건 :
//  @NotEmpty(message = "이메일은 필수 항목입니다.")
//  @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 주소를 입력해주세요.")
  private String email;

  //    # 조건 : 숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내
//  @NotEmpty(message = "비밀번호는 필수 항목입니다.")
//  @Size(min = 8, max = 15, message = "비밀번호는 8~15자리여야 합니다.")
//  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "숫자, 문자, 특수문자를 모두 포함해야 합니다.")
  private String password;

  //    # 조건 : 숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내
//  @NotEmpty(message = "다시한번 작성해주세요.")
//  @Size(min = 8, max = 15, message = "비밀번호는 8~15자리여야 합니다.")
//  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "숫자, 문자, 특수문자를 모두 포함해야 합니다.")
  private String checkPassword;

//  @NotEmpty(message = "이름은 필수 항목입니다.")
  private String name;

  private String address;

  //    # 조건 : 숫자와 하이픈(-)만 작성가능
//  @NotEmpty(message = "전화번호는 필수 항목입니다.")
//  @Pattern(regexp = "^[0-9-]+$", message = "숫자와 하이픈(-)만 입력해주세요.")
  private String phone;

  public void updatePassword(String password) {
    this.password = password;
  }

//  public boolean passwordsMatch() {
//    return password.equals(checkPassword);
//  }
}
