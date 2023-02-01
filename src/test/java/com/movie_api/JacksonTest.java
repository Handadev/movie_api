package com.movie_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_api.db.collection.TestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JacksonTest {
    @Test
    void classToJson() throws JsonProcessingException {
        TestDTO dto = new TestDTO();
        TestDTO.userList list1 = new TestDTO.userList("1", "one");
        TestDTO.userList list2 = new TestDTO.userList("2", "two");

        List<TestDTO.userList> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);

        dto.setUserList(list);

        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(dto);

        System.out.println("result = " + result);
        // {"userList":[{"id":"1","name":"one"},{"id":"2","name":"two"}]}
        // "{"userList":[{"id":"63bf5fa8713e6137e3857ad5","title":"아바타2"},{"id":"63bf5fa8713e6137e3857ad4","title":"타이타닉"},{"id":"63bf5fa8713e6137e3857ad3","title":"아바타"}]}"
    }
}
