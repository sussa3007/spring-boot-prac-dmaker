package com.pracex.programming.dmaker.exception;

import lombok.Getter;

// 커스텀 예외
@Getter
public class DMakerException extends RuntimeException {
    private DMakerErrorCode dMakerErrorCode;
    private String detailMessage;

    // 기본 메세지 생성자
    public DMakerException(DMakerErrorCode errorCode){
        super(errorCode.getMessage());
        this.dMakerErrorCode = errorCode;
        this.detailMessage = errorCode.getMessage();
    }
    // 에러 메세지까지 추가하는 생성자
    public DMakerException(DMakerErrorCode errorCode, String detailMessage){
        super(detailMessage);
        this.dMakerErrorCode = errorCode;
        this.detailMessage = detailMessage;
    }

}
