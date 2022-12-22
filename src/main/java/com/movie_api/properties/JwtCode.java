package com.movie_api.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtCode {
    ACCESS_TOKEN("access"),
    REFRESH_TOKEN("refresh");

    private final String type;

}
