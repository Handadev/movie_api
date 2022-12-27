package com.movie_api.db.collection;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Document(collection = "user")
public class User {
    @Transient
    public static final String SEQ_NAME = "user_seq";

    public User() {}
    @PersistenceCreator
    public User(String loginId, String pw) {
        this.loginId = loginId;
        this.pw = pw;
    }

    @PersistenceCreator
    public User(String loginId, String name, String mobileNo, String pw) {
        this.loginId = loginId;
        this.name = name;
        this.mobileNo = mobileNo;
        this.pw = pw;
        this.regDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
    }

    @Id
    private Long id;
    private String loginId;
    private String name;
    private String mobileNo;
    private String pw;
    private String regDate;
    private String loginDate;
}
