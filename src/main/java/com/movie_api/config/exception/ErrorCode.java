package com.movie_api.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, 100,"파라미터 값을 확인해주세요."),
    //500 예상치 못한 오류
    UNKNOWN_ERROR(500, 100,"YOU FOUND UNKNOWN ERROR.");

    private final int code;
    private final int subCode;
    private final String message;


}
