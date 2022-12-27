package com.movie_api.db.repo;

import com.movie_api.db.collection.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface UserTokenRepo extends MongoRepository<UserToken, BigInteger> {

    UserToken findById(int id);
}
