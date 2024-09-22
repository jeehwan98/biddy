package com.jee.back.auth.interceptor;

import com.jee.back.common.AuthConstants;
import com.jee.back.common.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.rmi.RemoteException;

@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("request: " + request);
        log.info("response: " + response);
        log.info("handler: " + handler);

        String header = request.getHeader(AuthConstants.AUTH_HEADER);
        String token = TokenUtil.splitHeader(header);

        log.info("header: " + header);
        log.info("token: " + token);

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
