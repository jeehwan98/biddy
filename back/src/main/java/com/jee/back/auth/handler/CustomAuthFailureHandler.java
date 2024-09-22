package com.jee.back.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        JSONObject jsonObject;
        String failMsg;

        if (exception instanceof AuthenticationServiceException) {
            failMsg = "userId doesn't exist";
        } else if (exception instanceof BadCredentialsException) {
            failMsg = "Incorrect password"; // userId or password is incorrect
        } else if (exception instanceof LockedException) {
            failMsg = "account is locked";
        } else if (exception instanceof DisabledException) {
            failMsg = "account is deactivated";
        } else if (exception instanceof AccountExpiredException) {
            failMsg = "account is expired";
        } else if (exception instanceof CredentialsExpiredException) {
            failMsg = "password is expired";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            failMsg = "failed to authenticate";
        } else if (exception instanceof UsernameNotFoundException) {
            failMsg = "userId doesn't exist";
        } else {
            failMsg = "unidentified error";
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("failType",failMsg);

        jsonObject = new JSONObject(resultMap);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        out.println(jsonObject);
        out.flush();
        out.close();
    }
}
