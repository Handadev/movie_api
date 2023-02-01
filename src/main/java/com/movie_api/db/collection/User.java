package com.movie_api.db.collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
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
    @Field("_id")
    private Long id;

    @Field("loginId")
    private String loginId;

    @Field("name")
    private String name;

    @Field("mobileNo")
    private String mobileNo;

    @Field("pw")
    @JsonIgnore
    private String pw;

    @Field("regDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

    @Field("loginDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginDate;
}
