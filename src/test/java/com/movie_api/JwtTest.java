package com.movie_api;


import com.movie_api.db.collection.User;
import com.movie_api.properties.JwtCode;
import com.movie_api.util.JwtService;
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

        String accessToken = jwtService.createAccessToken(user, JwtCode.ACCESS_TOKEN.getType());
        System.out.println("accessToken => " + accessToken);

        String exToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb3ZpZSIsImV4cCI6MTY3MTcyNjk3OSwidXNlcklkeCI6MSwidXNlcklkIjoiQUdTUXYxU3RQWjlJTDVPMlc3STRkQT09In0.LD7NduSXnVfOzxFlWyu9wi6LW-miXIz2JFs5HNiDjR4";

        Claims claims = jwtService.decodeToken(exToken);

        System.out.println("claims = " + claims);


    }
}
