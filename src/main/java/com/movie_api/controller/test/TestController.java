package com.movie_api.controller.test;

import com.movie_api.config.exception.CustomException;
import com.movie_api.config.exception.ErrorCode;
import com.movie_api.util.RestResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@AllArgsConstructor
public class TestController {



    @GetMapping(value = "/sample")
    public ResponseEntity<?> sample(@RequestParam String val) {

        if ("ex".equals(val)) {
            throw new CustomException(ErrorCode.INVALID_PARAMETER);
        }

        HashMap<String, String> a = new HashMap<>();
        a.put("aaa", "aaa");
        a.put("bbb", "aaa");
        a.put("ccc", "aaa");
        a.put("ddd", "aaa");
        a.put("eee", "aaa");


        return new RestResponse().ok().setBody(a).responseEntity();
    }
}
