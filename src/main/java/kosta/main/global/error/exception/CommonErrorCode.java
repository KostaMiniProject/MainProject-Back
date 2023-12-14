package kosta.main.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    /**
     * 400 Bad Request
     */

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_MULTIPART_FILE(HttpStatus.BAD_REQUEST, "유효하지 않은 파일입니다."),
    INVALID_FILE_UPLOAD(HttpStatus.BAD_REQUEST, "S3 파일 업로드에 실패했습니다."),
    INVALID_BID_REQUEST(HttpStatus.BAD_REQUEST,"입찰에는 최소한 하나의 물건 등록이 필요합니다"),
    INVALID_BIDDING_REQUEST(HttpStatus.BAD_REQUEST, "삭제된 물건은 입찰에 사용할 수 없습니다."),
    MY_POST_BID(HttpStatus.BAD_REQUEST, "교환 게시글 작성자는 입찰을 진행할 수 없습니다."),
    OTHER_ITEM_USE(HttpStatus.BAD_REQUEST, "본인의 물건만 입찰에 사용할 수 있습니다."),
    ALREADY_BIDDING_ITEM(HttpStatus.BAD_REQUEST,"이미 입찰에 사용중인 물건입니다."),
    NOT_BID_OWNER(HttpStatus.BAD_REQUEST, "입찰을 작성한 유저가 아닙니다."),
    NOT_EXCHANGE_POST_OWNER(HttpStatus.BAD_REQUEST, "교환 게시글 작성자가 아닙니다."),
    FINISHED_EXCHANGE(HttpStatus.BAD_REQUEST,"거래가 완료된 게시물입니다."),
    NOT_DENIED_STATUS(HttpStatus.BAD_REQUEST, "입찰이 DENIED 상태가 아닙니다."),
    NOT_COMMUNITY_POST_OWNER(HttpStatus.BAD_REQUEST, "사용자와 작성자가 일치하지 않습니다."),
    NOT_ITEM_OWNER(HttpStatus.BAD_REQUEST,"물건의 주인이 아닙니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다."),
    NOT_EQUAL_COMMUNITY_POST(HttpStatus.BAD_REQUEST, "부모댓글과 자식댓글의 커뮤니티 게시글이 일치하지 않습니다"),


    /**
     * 404 Not Found
     */

    BID_NOT_FOUND(HttpStatus.NOT_FOUND, "입찰을 찾을 수 없습니다."),
    SELECTED_BID_NOT_FOUND(HttpStatus.NOT_FOUND, "거래 선택된 입찰을 찾을 수 없습니다."),
    EXCHANGE_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "교환 게시글을 찾을 수 없습니다."),
    COMMUNITY_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "커뮤니티 게시글을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "물건을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"이메일을 찾을 수 없습니다."),

    /**
     * 405 Method Not Allowed
     */

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"잘못된 HTTP method 요청입니다."),

    /**
     *  409 Conflict
     */

    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),


    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
