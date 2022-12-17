package com.movie_api.db.collection;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@Document(collection = "user")
public class User {
    private String name;
    private String mobileNo;
}
