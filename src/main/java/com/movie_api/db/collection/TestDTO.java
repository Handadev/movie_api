package com.movie_api.db.collection;

import lombok.Getter;
import lombok.Setter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TestDTO {
    private List<userList> userList = new ArrayList<>();
    @Getter
    @Setter
    public static class userList {

        public userList() {

        }
        public userList(String id, String title) {
            this.id = id;
            this.title = title;
        }

        private String id;
        private String title;
    }

}
