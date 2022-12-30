package com.movie_api.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.movie_api.exception.CustomException;
import com.movie_api.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class HelperClass {

    public HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        return servletRequest;
    }

    public HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse servletResponse = ((ServletRequestAttributes) requestAttributes).getResponse();
        return servletResponse;
    }

    public String getParam(String key) {
        String parameter = getRequest().getParameter(key);
        if (parameter == null) throw new CustomException(ErrorCode.INVALID_PARAMETER).setErrorMsg(key);
        return parameter;
    }

    public String getParamOrDefault(String key, @Nullable String defaultVal) {
        String parameter = getRequest().getParameter(key);
        if (parameter == null) parameter = defaultVal == null ? null : defaultVal;
        return parameter;
    }

    public Integer getIntegerParam(String key) {
        String parameter = getRequest().getParameter(key);
        if (parameter == null) throw new CustomException(ErrorCode.INVALID_PARAMETER).setErrorMsg(key);
        return Integer.valueOf(parameter);
    }

    public Integer getIntegerParamOrDefault(String key, Integer defaultVal) {
        String parameter = getRequest().getParameter(key);
        if (parameter == null) return defaultVal;
        return Integer.valueOf(parameter);
    }

    public String getCookie(String name) {
        Cookie[] cookies = getRequest().getCookies();

        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public void setCookie(String name, @Nullable String value, @Nullable Integer age) {
        HttpServletResponse response = getResponse();

        Cookie cookie = new Cookie(name, value);

        // 쿠키 분단위 설정
        if (age != null) cookie.setMaxAge(age * 60);
        // value 없으면 쿠키 삭제
        if (value == null) cookie.setMaxAge(0);

        cookie.setPath("/");

        response.addCookie(cookie);
    }

    // Object list to Json list
    public List<Object> toJsonList(List<?> list) {
        if(list.isEmpty()) return null;
        List<Object> result = new ArrayList<>();
        list.forEach(obj -> {
            JsonNode jsonNode = strToJson(objToStr(obj));
            result.add(jsonNode);
        });
        return result;
    }

    // Object to Json object
    public JsonNode toJsonObj(HashMap<String, ?> obj) {
        if (obj.isEmpty()) return null;
        return strToJson(objToStr(obj));
    }


    // obj to jsonString
    public String objToStr(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // LocalDateTime 위한 세팅
        try {
            return mapper.writeValueAsString(obj);
        }  catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    // jsonString to json
    public JsonNode strToJson(String str) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(str);
            return jsonNode;
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    public String jsonToString (JsonNode node) {
        return nodeToString(node);
    }
    public String jsonToString (JsonNode node, String getParam) {
        return nodeToString(node.get(getParam));
    }

    public int jsonToInt (JsonNode node) {
        return Integer.parseInt(nodeToString(node));
    }

    public int jsonToInt (JsonNode node, String getParam) {
        return Integer.parseInt(nodeToString(node.get(getParam)));
    }

    public List<?> jsonArrToList (JsonNode node) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(node, ArrayList.class);
    }

    public List<?> jsonArrToList (JsonNode node, String getParam) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(node.get(getParam), ArrayList.class);
    }

    public String nodeToString(JsonNode node) {
        return node.toString().replaceAll("\"","");
    }

}
