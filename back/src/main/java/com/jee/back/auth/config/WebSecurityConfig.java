package com.jee.back.auth.config;

import com.jee.back.auth.filter.CustomAuthenticationFilter;
import com.jee.back.auth.filter.JwtAuthorizationFilter;
import com.jee.back.auth.handler.CustomAuthFailureHandler;
import com.jee.back.auth.handler.CustomAuthSuccessHandler;
import com.jee.back.auth.handler.CustomAuthenticationProvider;
import com.jee.back.common.URLConstants;
import com.jee.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final UserRepository userRepository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(URLConstants.PUBLIC_URLS).permitAll()
                        .requestMatchers(URLConstants.ADMIN_URLS).hasRole("ADMIN")
                        .requestMatchers(URLConstants.USERS_URLS).hasRole("USER")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(userRepository);
    }

    // need to implement these 2 before writing the customAuthenticationFilter logic
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthenticationProvider());
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    private CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthLoginSuccessHandler());
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailureHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    private CustomAuthSuccessHandler customAuthLoginSuccessHandler() {
        return new CustomAuthSuccessHandler();
    }

    private CustomAuthFailureHandler customAuthFailureHandler() {
        return new CustomAuthFailureHandler();
    }
}
