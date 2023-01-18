package com.movie_api.db.repo;

import com.movie_api.db.collection.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TestRepo extends MongoRepository<Test, String> {
    @Override
    List<Test> findAll();
}
