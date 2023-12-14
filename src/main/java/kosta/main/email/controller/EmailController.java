package kosta.main.email.controller;

import jakarta.validation.Valid;
import kosta.main.email.dto.EmailCheckDto;
import kosta.main.email.dto.EmailSendDto;
import kosta.main.email.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
  private final EmailSendService emailService;

  @PostMapping("/api/mailSend")
  public String mailSend(@RequestBody @Valid EmailSendDto emailSendDto) {
    System.out.println("##### 인증번호 이메일 전송 #####");
    System.out.println("이메일 :" + emailSendDto.getEmail());
    return emailService.joinEmail(emailSendDto.getEmail());
  }

  @PostMapping("/api/authNumCheck")
  public Boolean authNumCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
    System.out.println("##### 이메일 인증 요청 #####");
    System.out.println("입력값 : " + emailCheckDto.getEmailCheckNum());
    return emailService.emailAuthNumCheck(emailCheckDto);
  }
}