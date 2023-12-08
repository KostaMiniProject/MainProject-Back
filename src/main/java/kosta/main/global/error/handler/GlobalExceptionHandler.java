package kosta.main.global.error.handler;

import jakarta.validation.ConstraintViolationException;
import kosta.main.global.error.dto.ErrorBaseResponse;
import kosta.main.global.error.dto.ErrorValidationResponse;
import kosta.main.global.error.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Valid & Validated annotation의 binding error를 handling합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorValidationResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException {}", e.getDetailMessageArguments());
        Object[] detailMessageArguments = e.getDetailMessageArguments();
        Object detailMessageArgument = detailMessageArguments[1];
        final ErrorValidationResponse errorValidationResponse = ErrorValidationResponse.of(e,detailMessageArgument);
        return new ResponseEntity<>(errorValidationResponse, e.getStatusCode());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorBaseResponse> handleBadRequestException(final BadRequestException e) {
        log.warn(e.getMessage(), e);

        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorBaseResponse> handleUnauthorizedException(final UnauthorizedException e){
        log.warn(e.getMessage(), e);
        ResponseEntity<ErrorBaseResponse> errorBaseResponseResponseEntity = handleExceptionInternal(HttpStatus.UNAUTHORIZED, e.getMessage());
        log.info("error==========================", errorBaseResponseResponseEntity);
        return errorBaseResponseResponseEntity;
    }
    /**
     * RequestParam annotation의 binding error를 handling합니다.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorBaseResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(">>> handle: MethodArgumentTypeMismatchException ", e);
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(CommonErrorCode.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorBaseResponse> handleRestApiException(final RestApiException e) {
        log.warn(e.getMessage(), e);

        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorBaseResponse> constraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage(), e);

        return handleExceptionInternal(HttpStatus.BAD_REQUEST, e.getMessage());
    }


    private ResponseEntity<ErrorBaseResponse> handleExceptionInternal(ErrorCode errorCode) {
        ErrorBaseResponse errorResponse;
        if(errorCode instanceof AuthErrorCode){
            errorResponse = ErrorBaseResponse.of((AuthErrorCode) errorCode);
        }else{
            errorResponse = ErrorBaseResponse.of(errorCode);
        }
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(errorResponse);
    }

    private ResponseEntity<ErrorBaseResponse> handleExceptionInternal(HttpStatus httpStatus, String massage) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorBaseResponse.of(httpStatus, massage));
    }

}

