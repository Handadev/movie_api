package com.movie_api.db.repo;

import com.movie_api.db.collection.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
public interface UserRepo extends MongoRepository<User, String> {
    // 비밀번호는 출력하지 않음
    @Query(value = "{}", fields = "{'pw':0}")
    List<User> findAll();
    List<User> findByLoginId(String loginId);
    List<User> findById(int id);
    User insert(User user);

    User deleteById(int id);


}
