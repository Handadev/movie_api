package com.movie_api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.movie_api.common.HelperClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class HttpRequester extends HelperClass {
    private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    private List<String> headers = new LinkedList<>();
    private String url;

    public HttpRequester(String url) {
        this.url = url;
    }

    public HttpRequester addHeader (String key, String val) {
        headers.add(key);
        headers.add(val);
        return this;
    }

    private String[] requestHeaders () {
        return headers.toArray(new String[headers.size()]);
    }

    public HttpRequester addParam (String key, String val) {
        params.add(key, val);
        return this;
    }
    private String requestBuilderGET() {
        return UriComponentsBuilder
                .fromUriString(url)
                .queryParams(params)
                .toUriString();
    }

    private String requestBuilderPOST() {
        return UriComponentsBuilder
                .fromUriString("")
                .queryParams(params)
                .toUriString().substring(1);
    }

    public JsonNode get() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(requestBuilderGET()))
                    .GET()
                    .build();
            return response(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode getWithHeader() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .headers(requestHeaders())
                    .uri(new URI(requestBuilderGET()))
                    .GET()
                    .build();
            return response(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode post() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBuilderPOST()))
                    .build();
            return response(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode postWithHeader() {
        addHeader("Content-Type", "application/x-www-form-urlencoded");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .headers(requestHeaders())
                    .uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBuilderPOST()))
                    .build();
            return response(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode response(HttpRequest request) {
        try {
            HttpResponse<String> res = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Request       >>> {}", res.request());
            log.info("Request param >>> {}", params.toString());
            log.info("Response code >>> {}", res.statusCode());
            log.info("Response body >>> {}", res.body());
        return strToJsonNode(res.body());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
