package com.jee.back.auth.filter;

import com.jee.back.auth.DetailsUser;
import com.jee.back.common.TokenUtil;
import com.jee.back.common.URLConstants;
import com.jee.back.user.entity.User;
import com.jee.back.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String requestURI = request.getRequestURI();

        if (isPublicURL(requestURI)) {
            filterChain.doFilter(request,response);
            return;
        }

        String token = TokenUtil.resolveToken(request);
        try {
            if (token != null && TokenUtil.validateToken(token)) {
                Claims claims = TokenUtil.getClaimsFromToken(token);
                String userId = claims.get("userId", String.class);
                User user = userRepository.findByUserId(userId);

                DetailsUser setUserToAuthentication = new DetailsUser(user);

//                User user = new User();
//                user.setRole(role);
//                user.setUserId(userId);
//                DetailsUser detailsUser = new DetailsUser(user);

                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(setUserToAuthentication, null, setUserToAuthentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                filterChain.doFilter(request, response);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
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
            resultMessage = "token has expired";
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

    private boolean isPublicURL(String requestURI) {
        for (String url : URLConstants.PUBLIC_URLS) {
            if (requestURI.startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}
