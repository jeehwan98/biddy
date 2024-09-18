package com.jee.back.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jee.back.auth.DetailsUser;
import com.jee.back.common.AuthConstants;
import com.jee.back.common.TokenUtil;
import com.jee.back.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@RequiredArgsConstructor
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DetailsUser detailsUser = (DetailsUser) authentication.getPrincipal();
        User user = detailsUser.getUser();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        HashMap<String, Object> responseMap = new HashMap<>();
        if (user != null) {

            if (user.getUserStatus().equals("active")) {
                String token = TokenUtil.generateJwtToken(user);
                response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);
                responseMap.put("message", "login success");
                responseMap.put("role", role);
                responseMap.put("token", response.getHeader(AuthConstants.AUTH_HEADER));

                // set jwt into cookies directly
                Cookie cookie = new Cookie("Authorization", token);
                // cookie.setDomain("localhost");
                cookie.setValue("None");
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60); // 1 hour
                response.addCookie(cookie);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
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

    /*
    getting token from the front-end
    <<< front-end >>>
    fetch('http://example.com/login', {
        method: 'POST',
                body: JSON.stringify({ username: 'example', password: 'password' }),
        headers: {
            'Content-Type': 'application/json'
        }
    })
            .then(response => {
    const token = response.headers.get('Authorization');
        console.log('Token:', token);
        // Save the token in localStorage or sessionStorage for future requests
    });
    */

    /*
      <<< backend >>>
    * String token = response.getHeader(AuthConstants.AUTH_HEADER);
    * */
}
