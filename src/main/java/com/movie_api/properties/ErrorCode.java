package com.movie_api.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, 100,"{} 파라미터 값을 확인해주세요."),
    MAPPING_NOT_FOUND(404, 100, "존재하지 않는 요청입니다"),
    //500 예상치 못한 오류
    UNKNOWN_ERROR(500, 100, "YOU FOUND UNKNOWN ERROR."),
    NO_RESULT(200, 100, "결과값이 없습니다"),

    // USER ERROR
    ID_EXIST(200, 201, "이미가입된 아이디가 있습니다"),
    PW_LENGTH(200, 202, "비밀번호는 8 ~ 20 자리입니다"),
    USER_NOT_EXIST(200, 203, "계정이 존재하지 않습니다"),
    LOGIN_FAIL(200, 204, "로그인에 실패했습니다"),

    // TOKEN ERROR
    TOKEN_INVALID(400, 300, "토큰이 유효하지 않습니다"),
    TOKEN_EXPIRED(400, 301, "토큰이 만료되었습니다"),

    ;

    private final int code;
    private final int subCode;
    @Setter
    private String message;


}
