package kosta.main.email.controller;

import jakarta.validation.Valid;
import kosta.main.email.dto.EmailRequestDto;
import kosta.main.email.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {
  private final EmailSendService emailService;
  @PostMapping ("/api/mail-send")
  public String mailSend(@RequestBody @Valid EmailRequestDto emailDto){
    System.out.println("##### 이메일 인증 요청 #####");
    System.out.println("이메일 :"+emailDto.getEmail());
    return emailService.joinEmail(emailDto.getEmail());
  }


//  @PostMapping("/mailauthCheck")
//  public String AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
//    Boolean Checked=emailService.CheckAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum());
//    if(Checked){
//      return "ok";
//    }
//    else{
//      throw new NullPointerException("뭔가 잘못!");
//    }
//  }
}