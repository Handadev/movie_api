package com.movie_api.db.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "test")
public class Test {

    @Id
    String id;

    @Field("title")
    String title;
}
