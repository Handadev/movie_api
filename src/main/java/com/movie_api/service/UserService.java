package com.movie_api.service;

import com.movie_api.config.exception.CustomException;
import com.movie_api.config.exception.ErrorCode;
import com.movie_api.db.collection.User;
import com.movie_api.db.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    public HashMap<String, Object> findUser(String mobileNo) {
        List<User> users = userRepo.findByMobileNoLike(mobileNo);
        log.info("users ={}", users);
        if (users.isEmpty()) throw new CustomException(ErrorCode.NO_RESULT);

        HashMap<String, Object> data = new HashMap<>();
        data.put("userList", users);

        return new HashMap<>(){{
            put("userList", users.stream().toArray());
        }};
    }

}
