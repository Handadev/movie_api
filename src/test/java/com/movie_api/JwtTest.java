package com.movie_api;


import com.movie_api.db.collection.User;
import com.movie_api.util.jwt.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {

    @Autowired
    JwtService jwtService;

    @Test
    void tokenTest() {
        User user = new User();
        user.setId(1L);
        user.setLoginId("tester");

        String accessToken = jwtService.createAccessToken(user);
        System.out.println("accessToken => " + accessToken);

        Claims claims = jwtService.decodeToken(accessToken);

        System.out.println("claims = " + claims);


    }
}
