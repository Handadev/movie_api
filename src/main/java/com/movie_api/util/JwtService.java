package com.movie_api.util;

import com.movie_api.config.exception.CustomException;
import com.movie_api.db.collection.User;
import com.movie_api.properties.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class JwtService {
    private final Crypto crypto;

    private final String JWT_HEADER = "Authorization";
    private final String ISSUER = "movie";
    private final String SECRET = "p2s5v8y/B?E(H+MbQeThWmZq4t6w9z$C";
    private final int ACCESS_EXPIRE_TIME = 1000 * 60 * 10;            // 10분
    private final int REFRESH_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 30; // 30일
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));


    public String createAccessToken(User user, String tokenType) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + ACCESS_EXPIRE_TIME);
        return createToken(user, expireTime);
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date  expireTime = new Date(now.getTime() + REFRESH_EXPIRE_TIME);
        return createToken(user, expireTime);
    }

    public String createToken(User user, Date expireTime) {
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setExpiration(expireTime)
                .claim("idx", user.getId())
                .claim("id", crypto.encodeAES256(user.getLoginId()))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims decodeToken(String token) {
        try {
             return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .requireIssuer(ISSUER)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }
        return null;
    }

    public Claims decodeRequestToken(HttpServletRequest request) {
        String header = request.getHeader(JWT_HEADER);
        if (header.indexOf("Bearer") == -1) throw new CustomException(ErrorCode.TOKEN_INVALID);
        return decodeToken(header.replace("Bearer ", ""));
    }

    public Claims getClaims(String token) {
        return decodeToken(token);
    }


}
