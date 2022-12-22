package com.movie_api.service;

import com.movie_api.common.HelperClass;
import com.movie_api.config.exception.CustomException;
import com.movie_api.properties.ErrorCode;
import com.movie_api.db.MongoSeqGenerator;
import com.movie_api.db.collection.User;
import com.movie_api.db.repo.UserRepo;
import com.movie_api.util.Crypto;
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
    private final MongoSeqGenerator seqGenerator;
    public HashMap<String, Object> allUser() {
        List<User> users = userRepo.findAll();

        if (users.isEmpty()) throw new CustomException(ErrorCode.NO_RESULT);
        return new HashMap<>(){{
            put("userList", toJsonList(users));
        }};
    }

    public HashMap<String, Object> insertUser(User user) {
        List<User> registeredUser = userRepo.findByLoginId(user.getLoginId());
        // 이미 가입된 계정 체크
        if (!registeredUser.isEmpty()) throw new CustomException(ErrorCode.ID_EXIST);
        // 비밀번호 길이 체크
        if (user.getPw().length() < 8 || user.getPw().length() > 20) throw new CustomException(ErrorCode.PW_LENGTH);

        user.setId(seqGenerator.generateSeq(User.SEQ_NAME));
        user.setPw(Crypto.encodeSHA256(user.getPw()));

        userRepo.insert(user);

        return new HashMap<>();
    }

    public HashMap<String, Object> deleteUser(int userIdx) {
        List<User> registeredUser = userRepo.findById(userIdx);
        // 계정 없을시
        if (registeredUser.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_EXIST);

        userRepo.deleteById(userIdx);

        return new HashMap<>();
    }

    public HashMap<String, Object> userLogin(User user) {
        List<User> userInfo = userRepo.findByLoginId(user.getLoginId());
        // 계정 없을시
        if (userInfo.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_EXIST);
        // 비밀번호 일치하지 않을시
        if (user.getPw() != userInfo.get(0).getPw()) throw new CustomException(ErrorCode.LOGIN_FAIL);


        return new HashMap<>(){{

        }};
    }
}
