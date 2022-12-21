package com.movie_api.controller;

import com.movie_api.common.HelperClass;
import com.movie_api.db.collection.User;
import com.movie_api.response.RestResponse;
import com.movie_api.service.UserService;
import com.movie_api.util.Crypto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController extends HelperClass {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        HashMap<String, Object> user = userService.allUser();
        return new RestResponse().ok().setBody(user).responseEntity();
    }

    @PostMapping
    public ResponseEntity<?> insertUser() {
        String loginId = getParam("loginId");
        String name = getParam("name");
        String mobileNo = getParam("mobileNo");
        String pw = getParam("pw");

        return new RestResponse().ok()
                .setBody(userService.insertUser(
                new User(loginId, name, mobileNo, pw)
        )).responseEntity();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        return new RestResponse().ok()
                .setBody(userService.deleteUser(
                getIntegerParam("userIdx")
        )).responseEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin() {
        String loginId = getParam("loginId");
        String pw = getParam("pw");

        return new RestResponse().ok()
                .setBody(userService.userLogin(
                        new User(loginId, Crypto.encodeSHA256(pw))
                )).responseEntity();
    }
}
