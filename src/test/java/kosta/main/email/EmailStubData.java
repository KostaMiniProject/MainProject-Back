package kosta.main.email;

import kosta.main.email.dto.EmailCheckDto;
import kosta.main.email.dto.EmailSendDto;

public class EmailStubData {

    public EmailSendDto getEmailSendDto() {
        return EmailSendDto.of("hongildong@gmail.com");
    }
    public EmailCheckDto getEmailCheckDto() {
        return EmailCheckDto.of("hongildong@gmail.com","abcdef");
    }
}
