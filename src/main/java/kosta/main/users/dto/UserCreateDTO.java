package kosta.main.users.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserCreateDTO {

  //    # 조건 :
//    ^ : 시작
//    [0-9a-zA-Z] : 숫자, 소문자, 대문자
//    ([-_.] : 특수문자
//    ? : 앞 내용이 있거나 없음
//    .[a-zA-Z]{2,3} : 2~3개 소문자, 대문자로 구성
//    $ : 끝
//  @NotNull(message = "이름은 필수 항목입니다.")
//  @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
//      message = "이메일 형식을 지켜주세요.")
  private String email;

  //    # 조건 : 숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내
//  @NotNull(message = "이름은 필수 항목입니다.")
//  @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
//      message = "숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내로 작성해주세요.")
  private String password;

  //    # 조건 : 숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내
//  @NotNull(message = "이름은 필수 항목입니다.")
//  @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$",
//      message = "숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내로 작성해주세요.")
  private String checkPassword;

//  @NotNull(message = "이름은 필수 항목입니다.")
  private String name;

  private String address;

  //    # 조건 : 3글자 - 3~4글자 - 4글자
//  @NotNull(message = "이름은 필수 항목입니다.")
//  @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식을 지켜주세요.")
  private String phone;

  public void updatePassword(String password) {
    this.password = password;
  }
}
