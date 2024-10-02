package com.jee.biddy1.auth.controller;

import com.jee.biddy1.auth.dto.LoginDTO;
import com.jee.biddy1.auth.dto.RegisterDTO;
import com.jee.biddy1.auth.jwt.JwtProvider;
import com.jee.biddy1.user.entity.RefreshToken;
import com.jee.biddy1.user.entity.User;
import com.jee.biddy1.user.entity.UserRole;
import com.jee.biddy1.user.repository.UserRepository;
import com.jee.biddy1.user.service.RefreshTokenService;
import com.jee.biddy1.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        HashMap<String, Object> responseMap = new HashMap<>();
        User user = userService.findByUserId(loginDTO.getUserId());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "user doesn't exist"));
        } else if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "invalid password"));
        }

        response.addHeader("Set-Cookie",getRefreshToken(user).toString());
        responseMap.put("token", jwtProvider.generateToken(user));
        responseMap.put("message", "login success");

        return ResponseEntity.ok()
                .body(responseMap);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterDTO registerDTO) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.findByUserId(registerDTO.getUserId()).isPresent()) {
            response.put("message", "userId is already taken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        registerDTO.setPassword(encodedPassword);
        registerDTO.setRole(UserRole.USER);
        registerDTO.setCreatedDate(LocalDateTime.now());
        log.info("registerDTO: " + registerDTO);
        User registeredUser = userService.registerUser(registerDTO);

        if (registeredUser.getId() != 0) {
            response.put("message", "user registered");
        } else {
            response.put("message", "user wasn't registered");
        }

        return ResponseEntity.ok(response);
    }

    private ResponseCookie getRefreshToken(User user) {
        return ResponseCookie
                .from("accessToken", jwtProvider.generateToken(user))
                .path("/")
//                .sameSite("None")
                .sameSite("Lax")
                .secure(false) // true for https
                .httpOnly(true)
                .maxAge(Duration.ofMinutes(30).getSeconds()) // 30분
                .build();
    }
}
