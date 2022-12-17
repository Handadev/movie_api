package com.movie_api.db.repo;

import com.movie_api.db.collection.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String> {
    List<User> findByMobileNoLike(String mobileNo);
    List<User> findAll();
}
