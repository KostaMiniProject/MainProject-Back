package kosta.main.global.error.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.MethodArgumentNotValidException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ErrorValidationResponse {

  private int status;
  private Object errorMessage;

  public static ErrorValidationResponse of(MethodArgumentNotValidException e, Object errorMessage) {
    return new ErrorValidationResponse(e.getStatusCode().value(), errorMessage);
  }
}
