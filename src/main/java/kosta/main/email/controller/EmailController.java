package kosta.main.email.controller;

import jakarta.validation.Valid;
import kosta.main.email.dto.EmailCheckDto;
import kosta.main.email.dto.EmailSendDto;
import kosta.main.email.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailController {
  private final EmailSendService emailService;

  /**
   * 인증번호 이메일 전송
   * @param emailSendDto
   * @return
   */

  @PostMapping("/api/email-verification")
  public ResponseEntity<?> mailSend(@RequestBody @Valid EmailSendDto emailSendDto) {
    log.info("이메일 :" + emailSendDto.getEmail());
    emailService.sendEmailAuthNumber(emailSendDto.getEmail());
    return new ResponseEntity<>(HttpStatus.OK);
  }


  /**
   * 이메일 인증 요청
   * @param emailCheckDto
   * @return
   */
  @PostMapping("/api/authNumCheck")
  public ResponseEntity<?> authNumCheck(@RequestBody @Valid EmailCheckDto emailCheckDto) {
    log.info("입력값 : " + emailCheckDto.getEmailCheckNum());
    Boolean result = emailService.emailAuthNumCheck(emailCheckDto);
    log.info("인증 여부 : " + (result ? "완료" : "오류"));
    return new ResponseEntity<>(HttpStatus.OK);
  }
}