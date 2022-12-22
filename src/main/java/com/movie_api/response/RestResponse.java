package com.movie_api.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_api.properties.ErrorCode;
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

    private RestResponse addCorsHeader() {
        String origin = "*";
        String methods = "POST, GET, DELETE, PUT, OPTIONS";
        String maxAge = "3600";
        String headers = "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization";
        addHeader("Access-Control-Allow-Origin", origin);
        addHeader("Access-Control-Allow-Methods", methods);
        addHeader("Access-Control-Max-Age", maxAge);
        addHeader("Access-Control-Allow-Headers", headers);
        return this;
    }

    public RestResponse ok() {
        RestResponse instance = new RestResponse();
        instance.setCode(HttpStatus.OK.value());
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
        return this;
    }

    public ResponseEntity<String> responseEntity() {
        log.info("Response >>> {}", toJsonString());
        return new ResponseEntity<String>(toJsonString(), headers, HttpStatus.valueOf(this.code));
    }

    public RestResponse customException(ErrorCode e) {
        RestResponse instance = new RestResponse();
        instance.setCode(e.getCode());
        instance.setSubCode(e.getSubCode());
        instance.setMessage(e.getMessage());
        return instance;
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
