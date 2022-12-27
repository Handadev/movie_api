package com.movie_api.db.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Data
@Document(collection = "userTokens")
public class UserToken {

    public UserToken(Long id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
    }

    @Id
    private Long id;
    private String refreshToken;
}
