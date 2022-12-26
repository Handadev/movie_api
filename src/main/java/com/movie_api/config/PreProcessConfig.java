package com.movie_api.config;

import com.movie_api.preprocessor.RequestInterceptor;
import com.movie_api.preprocessor.TokenInterceptor;
import com.movie_api.util.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class PreProcessConfig implements WebMvcConfigurer {

    private final JwtService jwtService;
    private final String[] requestExclude = {
            "/static/**"
            , "/favicon.ico"
            , "/error"
    };

    private final String[] tokenExclude = {
            "/static/**"
            , "/favicon.ico"
            , "/error"
            , "/user/login"
            , "/user/logout"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(requestExclude)
                .order(0);

        registry.addInterceptor(new TokenInterceptor(jwtService))
                .addPathPatterns("/**")
                .excludePathPatterns(tokenExclude)
                .order(1);

    }

}
