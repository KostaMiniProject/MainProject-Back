package kosta.main.global.error.handler;

import jakarta.validation.ConstraintViolationException;
import kosta.main.global.error.dto.ErrorBaseResponse;
import kosta.main.global.error.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.BindException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice(basePackages = "kosta.main")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request
    ) {
        log.warn(e.getMessage(), e);

        final String errMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(ErrorBaseResponse.of(CommonErrorCode.BAD_REQUEST.getHttpStatus().value(), errMessage));
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
                .body(ErrorBaseResponse.of(httpStatus.value(), massage));
    }

    private ResponseEntity<ErrorBaseResponse> handleExceptionInternal(HttpStatusCode httpStatus, String massage) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorBaseResponse.of(httpStatus.value(), massage));
    }
}

