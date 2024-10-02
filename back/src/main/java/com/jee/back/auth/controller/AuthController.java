package com.jee.back.auth.controller;

import com.jee.back.auth.dto.LoginDTO;
import com.jee.back.auth.dto.RegisterDTO;
import com.jee.back.common.UserRole;
import com.jee.back.user.entity.User;
import com.jee.back.user.repository.UserRepository;
import com.jee.back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /** User Registration */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registration(@RequestBody RegisterDTO registerDTO) {
        Map<String, String> response = new HashMap<>();
        if (userRepository.existsByUserId(registerDTO.getUserId())) {
            response.put("message", "userId is already taken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            response.put("message", "email is already taken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        registerDTO.setPassword(encodedPassword);
        registerDTO.setRole(UserRole.USER);
//        registerDTO.setCreatedAt(LocalDate.now());
        registerDTO.setUserStatus("active");
        userService.registerUser(registerDTO);

        response.put("message", "User successfully registered");
        return ResponseEntity.ok().body(response);
    }

    /** Login */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {

        User fetchedUserInfo = (userRepository.findByUserId(loginDTO.getUserId())).get();

        if (userRepository.findByUserId(loginDTO.getUserId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User doesn't exist");
        } else if (!passwordEncoder.matches(loginDTO.getPassword(), fetchedUserInfo.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        } else {
            return ResponseEntity.ok("Login successful");
        }
    }
}
