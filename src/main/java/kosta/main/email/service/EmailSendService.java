package kosta.main.email.service;

import kosta.main.email.dto.EmailCheckDto;
import kosta.main.email.entity.Emails;
import kosta.main.email.repository.EmailsRepository;
import kosta.main.global.error.exception.BusinessException;
import kosta.main.global.error.exception.CommonErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
@AllArgsConstructor
public class EmailSendService {

  private final EmailsRepository emailsRepository;

  @Autowired
  private JavaMailSender javaMailSender;



  /**
   * 이메일 인증
   * @param emailCheckDto
   * @return
   */
  public Boolean emailAuthNumCheck(EmailCheckDto emailCheckDto) {
//    4. 인증번호 확인
    Emails emails = emailsRepository.findById(emailCheckDto.getEmail()).orElseThrow(() -> new BusinessException(CommonErrorCode.EMAIL_NOT_FOUND));
    boolean result = emails.getAuthNum().toString().equals(emailCheckDto.getEmailCheckNum());
    if (result) {
      emailsRepository.deleteById(emailCheckDto.getEmail());
    }
    return result;
  }



  /**
   * 임의의 6자리 양수 생성
   * @return
   */
  public Integer makeRandomNumber() {
    Random r = new Random();
    String randomNumber = "";
    for (int i = 0; i < 6; i++) {
      randomNumber += Integer.toString(r.nextInt(10));
    }

    return Integer.parseInt(randomNumber);
  }



  /**
   * 메일을 보내기 위한 상세 정보
   * @param email
   * @return
   */
  public String joinEmail(String email) {
    //    1. 임의의 6자리 수 생성
    Integer authNumber = makeRandomNumber();

    //    2. 이메일과 임의의 수를 Email 테이블의 저장
    Emails newEmailCheck = Emails.builder()
        .email(email)
        .authNum(authNumber)
        .build();
    emailsRepository.save(newEmailCheck);

    //    3. 입력된 메일에 인증 번호 전송
    String setFrom = "itsopshop2023@gmail.com";
    String toMail = email;
    String title = "회원 가입 인증 이메일 입니다.";
    String content =
        "가치잇솝 방문을 환영합니다 :)" +
            "<br><br>" +
            "인증 번호는 <strong>" + authNumber + "</strong> 입니다." +
            "<br>" +
            "인증번호를 제대로 입력해주세요!!!";
    mailSend(setFrom, toMail, title, content);
    return Integer.toString(authNumber);
  }



  /**
   * 이메일 전송 과정
   * @param setFrom
   * @param toMail
   * @param title
   * @param content
   */
  public void mailSend(String setFrom, String toMail, String title, String content) {
    MimeMessage message = javaMailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
    try {
      // # 이메일 메시지 관련 설정
      // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

      helper.setFrom(setFrom);    // 이메일 발신자 주소 설정
      helper.setTo(toMail);       // 이메일 수신자 주소 설정
      helper.setSubject(title);   // 이메일 제목 설정
      helper.setText(content, true);  // 이메일의 내용 설정, true값을 전달하여 html 형식의 메세지를 지원
      javaMailSender.send(message);

    } catch (MessagingException e) {
      // 이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류일 때
      e.printStackTrace();
    }
  }


}