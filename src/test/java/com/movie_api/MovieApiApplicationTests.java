package com.movie_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie_api.util.Crypto;
import com.movie_api.util.HttpRequester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MovieApiApplicationTests {

    @Autowired Crypto crypto;
    @Test
    void encrypt() {
        String text = "냠냠";


        String enc = crypto.encodeAES256(text);

        System.out.println("enc = " + enc);

        String dec = crypto.decodeAES256(enc);

        System.out.println("dec = " + dec);

    }

    @Test
    void httpRequest() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://127.0.0.1:18080/api/v1/device/board/list?page=1&targetId=board"))
                .header("Authorization", "!Q")
                .GET()
                .build();

        HttpResponse<String> res = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("res body = " + res.body());
        System.out.println("res headers = " + res.headers());
        System.out.println("res request = " + res.request());
        System.out.println("res statusCode = " + res.statusCode());

    }

    @Test
    void httpPostRequest() throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("loginId", "tester");
        params.put("password", "1234qwer");

//        System.out.println("params = " + getFormDataAsString(params));


        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(new URI("http://127.0.0.1:18080/api/v1/user/login"))
                .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(params)))
                .build();

        HttpResponse<String> res = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("res body = " + res.body());
        System.out.println("res headers = " + res.headers());
        System.out.println("res request = " + res.request());
        System.out.println("res statusCode = " + res.statusCode());
    }

    public String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }
    
    
    @Test
    void javaNetRequest() {
        HttpRequester requester = new HttpRequester("http://127.0.0.1:18080/api/v1/user/login");
        requester.addParam("loginId", "tester");
        requester.addParam("password", "1234qwer");

        JsonNode post = requester.post();
        System.out.println("post = " + post);
    }
//
    @Test
    void postRequest() {
        HttpRequester req = new HttpRequester("http://127.0.0.1:18080/api/v1/admin/notice");
        req.addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTm0iOiJHZFRtR1pOWC9kY0NGWitwNFVyYWZBPT0iLCJwZXJtaXNzaW9ucyI6IltYNVAsIEU1UCwgWDVQLCBFNVAsIFg1UCwgRTVQLCBYOVAsIEU5UCwgWDEwUCwgWDExUCwgRTEwUCwgRTExUCwgWDEwUCwgWDExUCwgRTEwUCwgRTExUCwgWDZQLCBYN1AsIEU2UCwgRTdQLCBYNlAsIFg3UCwgRTZQLCBFN1AsIFg2UCwgWDdQLCBFNlAsIEU3UCwgWDZQLCBYN1AsIEU2UCwgRTdQLCBYMVAsIEUxUCwgWDNQLCBFM1AsIFgxMlAsIEUxMlBdIiwiaXNzIjoiVEZTUmVzdEFwaSIsImV4cCI6MTY3MjIwNTE2MywidXNlcklkIjoiZGEwcGRORjdFblg0SWpScWFINElmUT09IiwiaWF0IjoxNjcxNjAwMzYzLCJ1c2VyU2VxIjo0fQ.cLb9tC1he8izwFEpcaE_y7GyMifDDaBxrM50-kfCOHw");

        req.addParam("content", "테스트");
        req.addParam("title", "테스트 제목");
        req.addParam("typeDiv", "N");
        req.addParam("visible", "Y");

        JsonNode jsonNode = req.postWithHeader();
        System.out.println("jsonNode = " + jsonNode);
    }
}
