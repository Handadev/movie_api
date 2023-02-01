package com.movie_api.service.v1;

import com.movie_api.common.HelperClass;
import com.movie_api.db.MongoSeqGenerator;
import com.movie_api.db.collection.Test;
import com.movie_api.db.collection.User;
import com.movie_api.db.collection.UserTokens;
import com.movie_api.db.repo.TestRepo;
import com.movie_api.db.repo.UserRepo;
import com.movie_api.db.repo.UserTokenRepo;
import com.movie_api.exception.CustomException;
import com.movie_api.exception.ErrorCode;
import com.movie_api.util.Crypto;
import com.movie_api.util.jwt.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService extends HelperClass {

    private final TestRepo testRepo;
    private final UserRepo userRepo;
    private final UserTokenRepo tokenRepo;
    private final MongoSeqGenerator seqGenerator;
    private final JwtService jwtService;

    @Cacheable(cacheNames = "users", cacheManager = "userCacheManager")
    public HashMap<String, Object> allUser() {
//        List<User> users = userRepo.findAll();
        List<Test> users = testRepo.findAll();
        if (users.isEmpty()) throw new CustomException(ErrorCode.NO_RESULT);
        return new HashMap<>(){{
            put("userList", toJsonList(users));
        }};
    }

    public HashMap<String, Object> insertUser(User user) {
        User registeredUser = userRepo.findByLoginId(user.getLoginId());
        // 이미 가입된 계정 체크
        if (!ObjectUtils.isEmpty(registeredUser)) throw new CustomException(ErrorCode.ID_EXIST);
        // 비밀번호 길이 체크
        if (user.getPw().length() < 8 || user.getPw().length() > 20) throw new CustomException(ErrorCode.PW_LENGTH);

        // _id autoIncrement
        user.setId(seqGenerator.generateSeq(User.SEQ_NAME));
        user.setPw(Crypto.encodeSHA256(user.getPw()));
        user.setRegDate(LocalDateTime.now());

        userRepo.save(user);

        return new HashMap<>();
    }

    public HashMap<String, Object> deleteUser(int userIdx) {
        User registeredUser = userRepo.findById(userIdx);
        // 계정 없을시
        if (ObjectUtils.isEmpty(registeredUser)) throw new CustomException(ErrorCode.USER_NOT_EXIST);

        userRepo.deleteById(userIdx);

        return new HashMap<>();
    }

    @Transactional
    public HashMap<String, Object> userLogin(User obj) {
        User userInfo = userRepo.findByLoginId(obj.getLoginId());

        // 계정 없을시
        if (ObjectUtils.isEmpty(userInfo)) throw new CustomException(ErrorCode.USER_NOT_EXIST);
        // 비밀번호 일치하지 않을시
        if (!obj.getPw().equals(userInfo.getPw())) throw new CustomException(ErrorCode.LOGIN_FAIL);


        // 사용자 로그인 일자 수정 저장
        userInfo.setLoginDate(LocalDateTime.now());
        userRepo.save(userInfo);

        // access refresh token 생성
        String accessToken = jwtService.createAccessToken(userInfo);
        String refreshToken = jwtService.createRefreshToken(userInfo);

        // 사용자 refreshToken 저장
        UserTokens tokenInfo = tokenRepo.findByUserIdx(userInfo.getId());
        if (ObjectUtils.isEmpty(tokenInfo)) {
            tokenRepo.save(new UserTokens(userInfo.getId(), refreshToken));
        } else {
            tokenInfo.setRefreshToken(refreshToken);
            tokenRepo.save(tokenInfo);
        }

        setCookie("refreshToken", refreshToken, null, true);

        return new HashMap<>(){{
            put("id", userInfo.getLoginId());
            put("accessToken", accessToken);
        }};
    }
}
