package kosta.main.global.error.dto;

import kosta.main.global.error.exception.AuthErrorCode;
import kosta.main.global.error.exception.CommonErrorCode;
import kosta.main.global.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ErrorBaseResponse {

  private int status;
  private String message;

  public static ErrorBaseResponse of(ErrorCode errorCode) {
    return ErrorBaseResponse.builder()
        .status(errorCode.getHttpStatus().value())
        .message(errorCode.getMessage())
        .build();
  }
  public static ErrorBaseResponse of(ErrorCode errorCode, String message) {
    return ErrorBaseResponse.builder()
        .status(errorCode.getHttpStatus().value())
        .message(message)
        .build();
  }
  public static ErrorBaseResponse of(HttpStatus httpStatus, String message) {
    return ErrorBaseResponse.builder()
        .status(httpStatus.value())
        .message(message)
        .build();
  }
}
