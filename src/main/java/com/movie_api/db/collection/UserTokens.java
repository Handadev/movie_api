package com.movie_api.db.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "userTokens")
public class UserTokens {

    public UserTokens(Long userIdx, String refreshToken) {
        this.userIdx = userIdx;
        this.refreshToken = refreshToken;
    }

    @Id
    private String id;
    @Field("userIdx")
    private Long userIdx;

    @Field("refreshToken")
    private String refreshToken;
}
