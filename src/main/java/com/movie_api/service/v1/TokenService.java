package com.movie_api.service.v1;

import com.movie_api.common.HelperClass;
import com.movie_api.db.collection.User;
import com.movie_api.db.repo.UserRepo;
import com.movie_api.exception.CustomException;
import com.movie_api.exception.ErrorCode;
import com.movie_api.util.Crypto;
import com.movie_api.util.jwt.JwtService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;

@Slf4j
@Service
@AllArgsConstructor
public class TokenService extends HelperClass {

    private final Crypto crypto;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    // 중복로그인 검사는 interceptor에서 진행하기 때문에 할 필요 없음
    public HashMap<String, Object> regenerateToken(String accessToken, String refreshToken) {
        // accessToken 만료 확인
        if(!jwtService.isTokenExpired(accessToken)) throw new CustomException(ErrorCode.ACCESS_TOKEN_NOT_EXPIRED);

        // refreshToken 만료 확인
        if(jwtService.isTokenExpired(refreshToken)) throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);

        // 유저확인
        Claims claims = jwtService.decodeToken(refreshToken);
        User user = userRepo.findByLoginId(crypto.decodeAES256(claims.get("id").toString()));

        if (ObjectUtils.isEmpty(user)) throw new CustomException(ErrorCode.USER_NOT_EXIST);

        // accessToken 재발급
        String token = jwtService.createAccessToken(user);

        return new HashMap<>(){{
            put("accessToken", token);
        }};
    }


}
