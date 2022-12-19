package com.movie_api.config;

import com.movie_api.filter.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    private final String[] excludeUrl = {
            "/static/**"
            , "/favicon.ico"
            , "/error"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludeUrl);
    }
}
