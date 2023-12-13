package kosta.main.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Random;

@Service
public class EmailSendService {
  @Autowired
  private JavaMailSender javaMailSender;
  private int authNumber;

//  @Autowired
//  private RedisUtil redisUtil;
//  public boolean CheckAuthNum(String email,String authNum){
//    if(redisUtil.getData(authNum)==null){
//      return false;
//    }
//    else if(redisUtil.getData(authNum).equals(email)){
//      return true;
//    }
//    else{
//      return false;
//    }
//  }

  // # 임의의 6자리 양수 생성
  public void makeRandomNumber() {
    Random r = new Random();
    String randomNumber = "";
    for (int i = 0; i < 6; i++) {
      randomNumber += Integer.toString(r.nextInt(10));
    }

    authNumber = Integer.parseInt(randomNumber);
  }


  // # 메일을 보내기 위한 상세 정보
  public String joinEmail(String email) {
    makeRandomNumber();
    String setFrom = "itsopshop2023@gmail.com";
    String toMail = email;
    String title = "회원 가입 인증 이메일 입니다.";
    String content =
        "가치잇솝에 방문해주셔서 감사합니다." +
            "<br><br>" +
            "인증 번호는 <strong>" + authNumber + "</strong> 입니다." +
            "<br>" +
            "인증번호를 제대로 입력해주세요"; //이메일 내용 삽입
    mailSend(setFrom, toMail, title, content);
    return Integer.toString(authNumber);
  }

  // # 이메일 전송 과정
  public void mailSend(String setFrom, String toMail, String title, String content) {
    MimeMessage message = javaMailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
    try {
      // # 이메일 메시지 관련 설정
      // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

      helper.setFrom(setFrom);    // 이메일 발신자 주소 설정
      helper.setTo(toMail);       // 이메일 수신자 주소 설정
      helper.setSubject(title);   // 이메일 제목 설정
      helper.setText(content, true);  // 이메일의 내용 설정, true값을 통해 html로 지정
      javaMailSender.send(message);

    } catch (MessagingException e) {
      // 이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
      e.printStackTrace();
    }


  }

}