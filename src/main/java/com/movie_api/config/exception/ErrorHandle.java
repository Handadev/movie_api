package com.movie_api.config.exception;

import com.movie_api.properties.ErrorCode;
import com.movie_api.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandle {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customException(CustomException e) {
        return new RestResponse().customException(e.getErrorCode()).responseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownException(Exception e) {
        e.printStackTrace();
        return new RestResponse().customException(ErrorCode.UNKNOWN_ERROR).responseEntity();
    }
}
