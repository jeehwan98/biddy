package com.jee.biddy1.auth.config;

import com.jee.biddy1.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
//
//    private final JwtProvider jwtProvider;
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedHeaders(
//                        HttpHeaders.AUTHORIZATION,
//                        HttpHeaders.CONTENT_TYPE,
//                        HttpHeaders.ACCEPT)
//                .allowedMethods(
//                        HttpMethod.GET.name(),
//                        HttpMethod.POST.name(),
//                        HttpMethod.PUT.name(),
//                        HttpMethod.DELETE.name())
//                .maxAge(jwtProvider.tokenExpireTime()) // convert to ms
//                .allowCredentials(true)
//                .exposedHeaders("*");
//    }
}
