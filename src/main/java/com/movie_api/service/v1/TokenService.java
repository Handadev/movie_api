package com.movie_api.service.v1;

import com.movie_api.config.exception.CustomException;
import com.movie_api.config.exception.ErrorCode;
import com.movie_api.util.jwt.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@AllArgsConstructor
public class TokenService {

    private JwtService jwtService;

    public HashMap<String, Object> regenerateToken(String accessToken) {
        // jwt token decode
        try {
            Claims claims = decodeExpireToken(accessToken);
            int userIdx = (Integer) claims.get("idx");
            log.info("userIdx >>>>>>>>>>>>>>>>>>>>>>>>>>>> {}", userIdx);
        } catch (ExpiredJwtException e) {
            log.info("userIdx >>>>>>>>>>>>>>>>>>>>>>>>>>>> 만룔오롱로어롱러오러");
        }
        // get userIdx

        // check refreshToken from db

        // regenerate refreshToken if refreshToken is Expired

        // regenerate accessToken

        return new HashMap<>();
    }

    public Claims decodeExpireToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtService.getSecret())
                    .requireIssuer(jwtService.getIssuer())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            log.error("Invalid Access JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid Access JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Access JWT token");
        } catch (IllegalArgumentException e) {
            log.error("Access JWT claims string is empty.");
        }
        throw new CustomException(ErrorCode.TOKEN_INVALID);
    }
}
