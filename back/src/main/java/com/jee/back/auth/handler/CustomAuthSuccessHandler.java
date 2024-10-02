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
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;


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
                response.setStatus(HttpServletResponse.SC_OK);
                String token = tokenUtil.generateJwtToken(user);

                responseMap.put("message", "login success");
                responseMap.put("roles", roles);
                responseMap.put("token", AuthConstants.TOKEN_TYPE + " " + token);
                log.info("token info: " + token);

                // set JWT into cookies directly
//                response.addHeader("Set-Cookie", setTokenToCookie(user));
//                Cookie loginCookie = new Cookie("Authorization", token);
//                loginCookie.setHttpOnly(true);
//                loginCookie.setSecure(false);   // need to set true during production**
//                loginCookie.setMaxAge(60 * 60); // 1시간
//                loginCookie.setPath("/");       // cookie available throughout the app
//                loginCookie.setDomain("localhost");
//                response.addCookie(loginCookie);

                // Set Authentication context
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } else {
            responseMap.put("message", "user is inactive");
        }

        // Send response body to the client
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseMap));
        response.getWriter().flush();
    }

    private ResponseCookie setTokenToCookie(User user) {
        return ResponseCookie
                .from("accessToken", tokenUtil.generateJwtToken(user))
                .path("/")
//                .sameSite("None")
                .sameSite("Lax")
                .secure(false) // true for https
                .maxAge(Duration.ofMinutes(30).getSeconds())
                .build();
    }
}
