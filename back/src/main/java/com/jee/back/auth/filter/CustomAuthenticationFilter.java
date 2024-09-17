package com.jee.back.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jee.back.auth.dto.LoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginDTO loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
            String userId = loginDTO.getUserId();
            String password = loginDTO.getPassword();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, password, null);

            return getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
