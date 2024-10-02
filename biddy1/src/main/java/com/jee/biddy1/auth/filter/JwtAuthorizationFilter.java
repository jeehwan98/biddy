package com.jee.biddy1.auth.filter;

import com.jee.biddy1.auth.jwt.JwtProvider;
import com.jee.biddy1.user.entity.User;
import com.jee.biddy1.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!request.getRequestURI().startsWith("/api/**")) {
            filterChain.doFilter(request, response);
            return;
        }
//        String path = request.getServletPath();
        if (request.getRequestURI().equals("/api/v1/login") || request.getRequestURI().equals("/api/v1/register") || request.getRequestURI().equals("api/v1/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

//        String accesstoken =
        
        String token = jwtProvider.resolveToken(request);
        log.info("token details????:::: " + token);

        try {
            if (token != null && JwtProvider.validateToken(token)) {
                String userId = jwtProvider.getUserIdFromToken(token);
                log.info("got userId??::: " + userId);
                User user = userService.findByUserId(userId);
                authenticate(user);
                filterChain.doFilter(request, response);
            } else {
                throw new NullPointerException();
            }
        }  catch (NullPointerException e) {
            handleException(response, e);
        }
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        PrintWriter printWriter = response.getWriter();
        JSONObject jsonObject = jsonResponseWrapper(e);
        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }

    private JSONObject jsonResponseWrapper(Exception e) {
        String resultMessage = "";

        if (e instanceof ExpiredJwtException) {
            resultMessage = "expired token";
        } else if (e instanceof SignatureException) {
            resultMessage = "token signature exception";
        } else if (e instanceof JwtException) {
            resultMessage = "token parsing JwtException";
        } else {
            resultMessage = "other token error";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("message", resultMessage);
        jsonMap.put("reason", e.getMessage());

        return new JSONObject(jsonMap);
    }

    // 강제 로그인 처리하는 method
    private void authenticate(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                user,
                null,
                user.getRole().getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);
    }
}
