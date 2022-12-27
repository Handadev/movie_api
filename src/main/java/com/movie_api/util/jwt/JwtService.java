package com.movie_api.util.jwt;

import com.movie_api.exception.CustomException;
import com.movie_api.db.collection.User;
import com.movie_api.exception.ErrorCode;
import com.movie_api.properties.JwtProperty;
import com.movie_api.util.Crypto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final Crypto crypto;
    private final JwtProperty jwtProperty;
    private final int ACCESS_EXPIRE_MINUTE = 10;  // 10분
    private final int REFRESH_EXPIRE_DATE = 30; // 30일
    @Getter
    private Key secret;
    @Getter
    private String issuer;

    @PostConstruct
    public JwtService init() {
        secret = Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8));
        issuer = jwtProperty.getIssuer();
        return this;
    }

    public String createAccessToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Date expireTime = Date.from(now.plusMinutes(ACCESS_EXPIRE_MINUTE).atZone(ZoneId.systemDefault()).toInstant());
        return createToken(user, now, expireTime);
    }

    public String createRefreshToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        Date expireTime = Date.from(now.plusDays(REFRESH_EXPIRE_DATE).atZone(ZoneId.systemDefault()).toInstant());
        return createToken(user, now, expireTime);
    }

    private String createToken(User user, LocalDateTime now, Date expireTime) {
        return Jwts.builder()
                .setIssuer(jwtProperty.getIssuer())
                .setExpiration(expireTime)
                .claim("idx", user.getId())
                .claim("id", crypto.encodeAES256(user.getLoginId()))
                .claim("dt", crypto.encodeAES256(now.toString()))
                .signWith(secret)
                .compact();
    }

    public Claims decodeRequestToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header.indexOf("Bearer") == -1) throw new CustomException(ErrorCode.TOKEN_INVALID);
        return decodeToken(header.replace("Bearer ", ""));
    }

    public Claims decodeToken(String token) {
        try {
             return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .requireIssuer(jwtProperty.getIssuer())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            log.error("Invalid Access JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid Access JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired Access JWT token");
            // token 재발급위한 오류처리
            throw new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Access JWT token");
        } catch (IllegalArgumentException e) {
            log.error("Access JWT claims string is empty.");
        }
        throw new CustomException(ErrorCode.TOKEN_INVALID);
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims result = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .requireIssuer(jwtProperty.getIssuer())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return false;
        } catch (SignatureException e) {
            log.error("Invalid Access JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid Access JWT token");
        }  catch (ExpiredJwtException e) {
            log.error("Expired Access JWT token");
            return true;
        }  catch (UnsupportedJwtException e) {
            log.error("Unsupported Access JWT token");
        } catch (IllegalArgumentException e) {
            log.error("Access JWT claims string is empty.");
        }
        throw new CustomException(ErrorCode.TOKEN_INVALID);
    }

}
