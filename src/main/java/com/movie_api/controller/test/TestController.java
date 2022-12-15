package com.movie_api.controller.test;

import com.movie_api.response.RestResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@AllArgsConstructor
public class TestController {



    @GetMapping(value = "/sample")
    public ResponseEntity<?> sample() {
        HashMap<String, String> a = new HashMap<>();
        a.put("aaa", "aaa");
        a.put("aaa", "aaa");
        a.put("aaa", "aaa");
        a.put("aaa", "aaa");
        a.put("aaa", "aaa");

        log.info("a = {}",a);
        RestResponse restResponse = new RestResponse();

        ResponseEntity<String> entity = restResponse.ok().setBody(a)
                .responseEntity();

        System.out.println("restResponse = " + entity);
        return new RestResponse().ok().setBody(a).responseEntity();
    }
}
