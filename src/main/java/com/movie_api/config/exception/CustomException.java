package com.movie_api.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    public CustomException setErrorMsg(String replaceStr) {
        errorCode.setMessage(errorCode.getMessage().replace("{}", replaceStr));
        return this;
    }

}
