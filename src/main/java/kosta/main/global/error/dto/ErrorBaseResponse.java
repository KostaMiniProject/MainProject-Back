package kosta.main.global.error.dto;

import kosta.main.global.error.exception.AuthErrorCode;
import kosta.main.global.error.exception.CommonErrorCode;
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

    public static ErrorBaseResponse of(Integer code, String message){
        return new ErrorBaseResponse(code, message);
    }

    public static ErrorBaseResponse of(ErrorCode errorCode){
        return new ErrorBaseResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }

    public static ErrorBaseResponse of(AuthErrorCode authErrorCode){
        return new ErrorBaseResponse(authErrorCode.getCode(), authErrorCode.getMessage());
    }
}
