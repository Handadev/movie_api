package com.movie_api.controller.v1;

import com.movie_api.common.HelperClass;
import com.movie_api.exception.CustomException;
import com.movie_api.exception.ErrorCode;
import com.movie_api.util.RestResponse;
import com.movie_api.service.v1.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class TokenController extends HelperClass {

    private final TokenService tokenService;

    @PostMapping("/regenerate")
    public ResponseEntity<?> regenerateToken() {
        String accessToken =  getRequest().getParameter("accessToken");
        String refreshToken = getRequest().getParameter("refreshToken");
        if (refreshToken == null || accessToken == null) throw new CustomException(ErrorCode.TOKEN_NULL);

        return new RestResponse().ok()
                .setBody(tokenService.regenerateToken(accessToken, refreshToken))
                .responseEntity();
    }
}
