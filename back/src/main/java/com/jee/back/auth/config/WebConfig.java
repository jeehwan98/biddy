package com.jee.back.auth.config;

import com.jee.back.auth.filter.HeaderFilter;
import com.jee.back.auth.interceptor.JwtTokenInterceptor;
import com.jee.back.common.AuthConstants;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/static/", "classpath:/public/", "classpath:/", "classpath:/resources/",
            "classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Bean
    public FilterRegistrationBean<HeaderFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<HeaderFilter>(createHeaderFilter()); // filter 정보를 줄 수 있겠금 해주는 method
        // indicates that the filter should have the lowest possible order, meaning it will be one of the first filters to be executed.
        // setting the order ensures that HeaderFilter is executed early in the filter chain, allowing it to intercept requests and modify responses before other filters or servlets are invoked
        registrationBean.setOrder(Integer.MIN_VALUE);
        // specifies the URL patterns for which the filter should be applied... "/*" -> indicates that the filter should be applied to all URL patterns, meaning that it will intercept all incoming requests
        registrationBean.addUrlPatterns("/*");
        return registrationBean; // register as a bean -> meaning that the servlet container will use this bean to instantiate and configure the "HeaderFilter" according to the specified settings.
    }

    @Bean
    public HeaderFilter createHeaderFilter() {
        return new HeaderFilter();
    }

    @Bean
    public JwtTokenInterceptor jwtTokenInterceptor() {
        return new JwtTokenInterceptor();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true) // allows cookie
                .exposedHeaders("Authorization");
    }
}
