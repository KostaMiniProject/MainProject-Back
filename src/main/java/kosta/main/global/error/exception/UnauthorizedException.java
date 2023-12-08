package kosta.main.global.error.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{



    public UnauthorizedException() {
        super(String.valueOf(AuthErrorCode.UNAUTHORIZED));
    }

    public UnauthorizedException(CommonErrorCode commonErrorCode) {
        super(String.valueOf(commonErrorCode));
    }
    public UnauthorizedException(AuthErrorCode authErrorCode) {
        super(String.valueOf(authErrorCode));
    }
}
