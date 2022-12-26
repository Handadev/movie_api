package com.movie_api.preprocessor;

import com.movie_api.common.HelperClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Slf4j
public class RequestInterceptor extends HelperClass implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // header
        Enumeration<String> headerNames = request.getHeaderNames();
        HashMap<String, Object> headerMap = new LinkedHashMap<>();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headerMap.put(header, request.getHeader(header));
        }

        // parmam
        HashMap<String, Object> paramMap = new LinkedHashMap<>();
        request.getParameterMap().forEach((key, val) -> {
            paramMap.put(key, Arrays.stream(val).collect(Collectors.joining()));
        });

        log.info("Request url    >>> {}", request.getRequestURI());
        log.info("Requset Header >>> {}", headerMap);
        log.info("Requset param  >>> {}", paramMap);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
