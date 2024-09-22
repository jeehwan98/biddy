package com.jee.back.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jee.back.auth.DetailsUser;
import com.jee.back.common.AuthConstants;
import com.jee.back.common.TokenUtil;
import com.jee.back.user.dto.UserDTO;
import com.jee.back.user.entity.User;
import com.jee.back.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DetailsUser detailsUser = (DetailsUser) authentication.getPrincipal();
        User user = detailsUser.getUser();
        List<GrantedAuthority> roles = detailsUser.getAuthorities().stream()
                .map(item -> new SimpleGrantedAuthority(item.getAuthority()))
                .collect(Collectors.toList());

        HashMap<String, Object> responseMap = new HashMap<>();
        if (user != null) {

            if (user.getUserStatus().equals("active")) {
                String token = TokenUtil.generateJwtToken(user);
                response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
                responseMap.put("message", "login success");
                responseMap.put("roles", roles);
                responseMap.put("token", response.getHeader(AuthConstants.AUTH_HEADER));
                log.info("token info: " + token);

                // set jwt into cookies directly
                Cookie loginCookie = new Cookie("Authorization", token);
                loginCookie.setHttpOnly(true);
                loginCookie.setSecure(false);
                loginCookie.setPath("/");
                loginCookie.setValue(token);
                loginCookie.setMaxAge(60 * 60); // 1 hour
                response.addCookie(loginCookie);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } else {
            responseMap.put("message", "user is inactive");
        }
        // this logic is required to send response body to the client.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseMap));
        response.getWriter().flush();
    }
}
