package com.jee.biddy1.auth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jee.biddy1.common.AuthConstants;
import com.jee.biddy1.user.entity.User;
import com.jee.biddy1.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class JwtProvider {

    private static String SECRET_KEY;

    private static int EXPIRE_TIME;

    @Value("${security.jwt.secret-key}")
    public void tokenSecretKey(String jwtSecretKey) {
        this.SECRET_KEY = jwtSecretKey;
    }

    @Value("${security.jwt.expiration-time}")
    public void tokenExpireTime(int jwtExpiration) {
        this.EXPIRE_TIME = jwtExpiration;
    }

    public int tokenExpireTime() {
        return EXPIRE_TIME;
    }

//    public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey, @Value("${security.jwt.expiration-time}") long expireTime) {
//        this.SECRET_KEY = secretKey;
//        this.EXPIRE_TIME = expireTime;
//    }

//    private SecretKey getSecretKey() {
//        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyOriging.getBytes());
//        return Keys.hm
//    }

    /** authentication 기반 토큰 생성 method */
    public String generateTokenUsingAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateToken(user);
    }

    /** userId 및 authorities 기반 토큰 생성 method */
    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .setClaims(createClaims(user))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpireDate())
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .compact();
    }

    public static Key createSignature() {
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        return new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
    }

    private static Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            String createdDateJson = objectMapper.writeValueAsString(user.getCreatedDate());
            claims.put("createdDate", createdDateJson); // localdatetime -> string으로 저장
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        claims.put("userId", user.getUserId());
        claims.put("imageUrl", user.getImageUrl());
        claims.put("role", user.getRole());

        return claims;
    }

    public static Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public static String getUserIdFromToken(String token) {
        String userId = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return userId;
    }

//    public Authentication getAuthentication(String token) {
//        return new UsernamePasswordAuthenticationToken(getUserId(token), "", createAuthorityList(getRole(token)))
//    }


    public static boolean validateToken(String token) {
        if (token == null) {
            return false;
        }

        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /** 사용자가 보낸 request header의 'Authorization' field에서 토큰 추출하는 method */
    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConstants.AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(AuthConstants.TOKEN_TYPE)) {
            return bearerToken.substring(7); // return token without 'BEARER '
        }
        return null;
    }

    private String getUserId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private UserRole getRole(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("role", UserRole.class);
    }

    private static Date getExpireDate() {
        Date now = new Date();
        return new Date(now.getTime() + EXPIRE_TIME);
    }
}
