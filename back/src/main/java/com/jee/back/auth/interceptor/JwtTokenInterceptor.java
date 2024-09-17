package com.jee.back.auth.interceptor;

import com.jee.back.common.AuthConstants;
import com.jee.back.common.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.rmi.RemoteException;

public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("request" + request);
        System.out.println("response" + response);
        System.out.println("filter" + handler);
        System.out.println("JwtTokenInterceptor 도착");

        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtil.splitHeader(header);

        System.out.println("header: " + header);
        System.out.println("token: " + token);

        if (token != null) {
            if (TokenUtil.validateToken(token)) {
                return true;
            } else {
                throw new RemoteException("expired token");
            }
        } else {
            throw new RemoteException("token info doesn't exist");
        }
    }
}
