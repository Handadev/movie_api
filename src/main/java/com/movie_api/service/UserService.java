package com.movie_api.service;

import com.movie_api.common.HelperClass;
import com.movie_api.config.exception.CustomException;
import com.movie_api.config.exception.ErrorCode;
import com.movie_api.db.collection.User;
import com.movie_api.db.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService extends HelperClass {

    private final UserRepo userRepo;
    public HashMap<String, Object> allUser() {
        List<User> users = userRepo.findAll();

        if (users.isEmpty()) throw new CustomException(ErrorCode.NO_RESULT);
        return new HashMap<>(){{
            put("userList", toJsonList(users));
        }};
    }

}
