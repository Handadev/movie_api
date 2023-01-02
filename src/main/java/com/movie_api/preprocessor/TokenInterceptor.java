package com.movie_api.preprocessor;

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
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@AllArgsConstructor
public class TokenInterceptor extends HelperClass implements HandlerInterceptor {

    private final JwtService jwtService;
    private final Crypto crypto;
    private final UserRepo userRepo;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // accessToken check
        Claims accessClaim = jwtService.decodeRequestToken(request);

        // check registered user
        String loginId = crypto.decodeAES256(accessClaim.get("id").toString());
        String accessLoginDate = crypto.decodeAES256(accessClaim.get("dt").toString());
        User userInfo = userRepo.findByLoginId(loginId);
        if (ObjectUtils.isEmpty(userInfo)) throw new CustomException(ErrorCode.TOKEN_INVALID);

        // get refreshToken
        String refreshToken = getCookie("refreshToken");
        Claims refreshClaim = jwtService.decodeToken(refreshToken);
        String refreshLoginDate = crypto.decodeAES256(refreshClaim.get("dt").toString());

        // throw error if login is duplicated
        if(!accessLoginDate.equals(refreshLoginDate)) throw new CustomException(ErrorCode.DOUBLE_LOGIN);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
