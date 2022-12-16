package com.movie_api.db.collection;

import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Document(collection = "user")
public class User {
    private String name;
    private String mobileNo;
}
