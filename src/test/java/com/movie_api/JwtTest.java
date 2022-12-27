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
        user.setId(3L);
        user.setLoginId("tester1");

        String accessToken = jwtService.createAccessToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        System.out.println("accessToken  => " + accessToken);
        System.out.println("refreshToken => " + refreshToken);

        // eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb3ZpZSIsImV4cCI6MTY3MjExNjk2NSwiaWR4IjozLCJpZCI6IkxCZE5Lb0MvWlI0S0R4cW1YWVZ0N1E9PSIsImR0IjoieXVUa0JjaUt0L3BVSWtyTGlHSkgrWTNhSEJkSVJVbEpIK1FsYURCOUxvQT0ifQ.5sqLCebiSKw_KIi1RxBQK2-zCdJq-A71fBStdE8cKBI
        // eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb3ZpZSIsImV4cCI6MTY3NDcwODkwNSwiaWR4IjozLCJpZCI6IkxCZE5Lb0MvWlI0S0R4cW1YWVZ0N1E9PSIsImR0IjoieXVUa0JjaUt0L3BVSWtyTGlHSkgrWms0Q3FKdXI0cFdVellpenlrQUhFMD0ifQ.4aixMrgVzQNiEgG1KX0MM5cwJ3TvFQKcemRHPIcYBjk


        Claims claims = jwtService.decodeToken(accessToken);

        System.out.println("claims = " + claims);


    }
}
