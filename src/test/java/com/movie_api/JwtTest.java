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

        String accessToken = jwtService.generateToken(user, JwtCode.ACCESS_TOKEN.getType());
//        System.out.println("accessToken => " + accessToken);

        String exToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb3ZpZSIsImlhdCI6MTY3MTY5NzEwOSwidXNlcklkeCI6MSwidXNlcklkIjoiQUdTUXYxU3RQWjlJTDVPMlc3STRkQT09In0.5BoYQBVyiAIRBEYsJQBCELXIMH6pzKyL_FxNi0XMvs0";


        Claims claims = (Claims) jwtService.validAndDecodeToken(exToken);

        System.out.println("claims = " + claims);


    }
}
