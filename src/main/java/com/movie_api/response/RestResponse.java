package com.movie_api.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;


@Slf4j
public class RestResponse {
    private transient HttpHeaders headers = new HttpHeaders();
    @Getter
    private HashMap<String, ?> data = new HashMap<>();
    @Setter @Getter
    private int code = 0;
    @Setter @Getter
    private int subCode = 0;
    @Setter @Getter
    private String message = "";

    public RestResponse(){
        addHeader("Content-Type", "application/json");
        addHeader("charset", "utf-8");
        addCorsHeader();
    }
    public RestResponse ok() {
        RestResponse instance = new RestResponse();
        instance.setCode(HttpStatus.OK.value());
        System.out.println(" = " + instance.code);
        return instance;
    }

    public RestResponse bad() {
        RestResponse instance = new RestResponse();
        instance.setCode(HttpStatus.BAD_REQUEST.value());
        return instance;
    }
    private RestResponse addHeader(String name, String value) {
        headers.set(name, value);
        return this;
    }

    public RestResponse setBody (HashMap<String, ?> data) {
        this.data = data;
        log.info("data ={}", this.data);
        return this;
    }

    public ResponseEntity<String> responseEntity() {
        log.info("toJsonString ={}", toJsonString());
        log.info("headers ={}", headers);
        log.info("code ={}", this.code);
        return new ResponseEntity<String>(toJsonString(), headers, HttpStatus.valueOf(this.code));
    }

    public RestResponse customException(int code , int subCode, String message) {
        RestResponse instance = new RestResponse();
        instance.setCode(code);
        instance.setSubCode(subCode);
        instance.setMessage(message);
        return this;
    }

    private RestResponse addCorsHeader() {
        String localOrigin = "*";
        String localMethods = "POST, GET, DELETE, PUT, OPTIONS";
        String localMaxAge = "3600";
        String localHeaders = "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization";
        addHeader("Access-Control-Allow-Origin", localOrigin);
        addHeader("Access-Control-Allow-Methods", localMethods);
        addHeader("Access-Control-Max-Age", localMaxAge);
        addHeader("Access-Control-Allow-Headers", localHeaders);
        return this;
    }

    private String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
    }

}
