package com.movie_api.db.repo;

import com.movie_api.db.collection.UserTokens;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface UserTokenRepo extends MongoRepository<UserTokens, String> {

    UserTokens findByUserIdx(Long idx);
}
