package com.jee.back.common;

import com.jee.back.user.entity.User;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TokenUtil {

    private static String jwtSecretKey;
    private static long jwtExpiration;

    @Value("${security.jwt.secret-key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        TokenUtil.jwtSecretKey = jwtSecretKey;
    }

    @Value("${security.jwt.expiration-time}")
    public void setTokenValidateTime(long jwtExpiration) {
        TokenUtil.jwtExpiration = jwtExpiration;
    }

    public static String generateJwtToken(User userInfo) {
        Date expireTime = new Date(System.currentTimeMillis() + jwtExpiration);
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(userInfo))
                .setSubject(userInfo.getUserId())
                .signWith(SignatureAlgorithm.HS256, createSignature())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expireTime);

        return builder.compact();
    }

    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("type", "jwt");
        header.put("alg", "HS256");
        header.put("date", System.currentTimeMillis());

        return header;
    }

    private static Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        claims.put("imageUrl", user.getImageUrl());
        claims.put("role", user.getRole());
        claims.put("userStatus", user.getUserStatus());
        claims.put("userId", user.getUserId());

        return claims;
    }

    private static Key createSignature() {
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        return new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
    }

    public static String splitHeader(String header) {
        log.info("split header");
        if (header != null) {
            return header.split(" ")[1];
        } else {
            return null;
        }
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            } else {
                return true;
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return false;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Claims getClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConstants.AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(AuthConstants.TOKEN_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
