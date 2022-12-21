package com.movie_api.db.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "db_sequence")
public class DBSequence {

    @Id
    private String id;
    private Long seq;

}
