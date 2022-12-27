package com.movie_api.db.collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Document(collection = "user")
public class User {
    @Transient
    public static final String SEQ_NAME = "user_seq";

    public User() {}

    public User(String loginId, String pw) {
        this.loginId = loginId;
        this.pw = pw;
    }

    public User(String loginId, String name, String mobileNo, String pw) {
        this.loginId = loginId;
        this.name = name;
        this.mobileNo = mobileNo;
        this.pw = pw;
    }

    @Id
    private Long id;
    private String loginId;
    private String name;
    private String mobileNo;
    private String pw;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;
}
