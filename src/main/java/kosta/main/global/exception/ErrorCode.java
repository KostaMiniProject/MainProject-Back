package kosta.main.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ACCESS_DENIED("ACCESS_001", "비공개 게시글은 작성자만 볼 수 있습니다.");

    private final String code;
    private final String message;
}
