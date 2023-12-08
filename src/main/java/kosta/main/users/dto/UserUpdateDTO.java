package kosta.main.users.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kosta.main.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateDTO {

  //    # 조건 : 숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내
//  @Size(min = 8, max = 15, message = "비밀번호는 8~15자리여야 합니다.")
//  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "숫자, 문자, 특수문자를 모두 포함해야 합니다.")
  private String password;

  //    # 조건 : 숫자, 문자, 특수문자(!@#$%^&+=) 포함 8~15자리 이내
//  @Size(min = 8, max = 15, message = "비밀번호는 8~15자리여야 합니다.")
//  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "숫자, 문자, 특수문자를 모두 포함해야 합니다.")
  private String checkPassword;

  private String name;

  private String address;

  //  # 조건 : 숫자와 하이픈(-)만 작성가능
//  @Pattern(regexp = "^[0-9-]+$", message = "숫자와 하이픈(-)만 입력해주세요.")
  private String phone;

  private String profileImage;

  private User.UserStatus userStatus;

  public void updateProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }
}
