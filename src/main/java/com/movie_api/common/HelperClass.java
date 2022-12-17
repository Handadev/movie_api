package com.movie_api.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HelperClass {

    // obj to jsonString
    public String objToStr(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        }  catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // jsonString to json
    public JsonNode strToJsonNode (String str) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(str);
            return jsonNode;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // Object list to Json list
    public List<Object> toJsonList(List<?> list) {
        if(list.isEmpty()) return null;
        List<Object> result = new ArrayList<>();
        list.forEach(obj -> {
            JsonNode jsonNode = strToJsonNode(objToStr(obj));
            result.add(jsonNode);
        });
        return result;
    }
}
