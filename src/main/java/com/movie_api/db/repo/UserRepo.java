package com.movie_api.db.repo;

import com.movie_api.db.collection.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;
import java.util.List;
public interface UserRepo extends MongoRepository<User, BigInteger> {
    // R
    // 비밀번호는 출력하지 않음
    @Query(value = "{}", fields = "{'pw':0}")
    List<User> findAll();
    User findByLoginId(String loginId);
    User findById(int id);

    // D
    User deleteById(int id);

}
