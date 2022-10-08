package com.pracex.programming.dmaker.exception;

import com.pracex.programming.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.pracex.programming.dmaker.exception.DMakerErrorCode.*;

// 전체 컨트롤러의 어드바이스 Bean 등록
@RestControllerAdvice
@Slf4j
public class DMakerExceptionHandler {
//    @ResponseStatus(value = HttpStatus.CONFLICT)
    // 상태코드 변경
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request){
        // Global Error 처리 1
        log.error("errorCode : {} , url : {} , message : {}",
                e.getDMakerErrorCode(),
                request.getRequestURI(),
                e.getDetailMessage());
        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }
    // 잘못된 request Method 예외처리
    // 최초 정보 Null
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handleBadRequest(
            Exception e , HttpServletRequest request
    ){
        log.error("url : {} , message : {}",
                request.getRequestURI(),
                e.getMessage());
        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();

    }
    // 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(
            Exception e , HttpServletRequest request
    ){
        log.error("url : {} , message : {}",
                request.getRequestURI(),
                e.getMessage());
        return DMakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();

    }
}
