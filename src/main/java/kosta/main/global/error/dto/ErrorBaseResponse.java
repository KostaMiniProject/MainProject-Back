package kosta.main.global.error.dto;

import kosta.main.global.error.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
}
