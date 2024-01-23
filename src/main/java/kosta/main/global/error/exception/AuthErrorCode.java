package kosta.main.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // 400 값이 일치하지 않는 경우
    INVALID_IP(HttpStatus.BAD_REQUEST,400, "접속한 위치가 동일하지 않습니다"),


    // 401 UNAUTHORIZED 인증 정보가 없는 경우
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401,"유효한 인증 정보가 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 4011,"유효하지 않은 JWT 서명입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 4012,"만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, 4013,"지원하지 않는 JWT 토큰입니다."),
    ILLEGAL_ARGUMENT_TOKEN(HttpStatus.UNAUTHORIZED, 4014,"잘못된 JWT 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 4015,"유효하지 않은 Refresh 토큰입니다."),

    // 403 FORBIDDEN 권한이 없는 경우
    FORBIDDEN(HttpStatus.FORBIDDEN, 403,"해당 요청에 대한 권한이 없습니다."),

    // 이메일 인증 관련
    COOL_TIME_SEND_EMAIL(HttpStatus.BAD_REQUEST, 600,"이메일 재전송 대기 시간입니다."),
    UNAUTHENTICATED_EMAIL(HttpStatus.BAD_REQUEST, 601,"인증되지 않은 이메일입니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
