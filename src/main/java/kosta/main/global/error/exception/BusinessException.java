package kosta.main.global.error.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private final ErrorCode commonErrorCode;
    public BusinessException(String message, CommonErrorCode commonErrorCode) {
        super(message);
        this.commonErrorCode = commonErrorCode;
    }

    public BusinessException(CommonErrorCode commonErrorCode) {
        super(commonErrorCode.getMessage());
        this.commonErrorCode = commonErrorCode;
    }
    public BusinessException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getMessage());
        this.commonErrorCode = authErrorCode;
    }

}
