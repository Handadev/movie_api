package com.movie_api.util.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtCode {
    ACCESS_TOKEN("access"),
    REFRESH_TOKEN("refresh");

    private final String type;

}
