package com.jee.biddy1.auth.controller;

import com.jee.biddy1.auth.dto.RegisterDTO;
import com.jee.biddy1.user.entity.User;
import com.jee.biddy1.user.entity.UserRole;
import com.jee.biddy1.user.repository.UserRepository;
import com.jee.biddy1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterDTO registerDTO) {
        Map<String, String> response = new HashMap<>();
        if (!userRepository.findByUserId(registerDTO.getUserId()).isPresent()) {
            response.put("message", "userId is already taken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        registerDTO.setPassword(encodedPassword);
        registerDTO.setRole(UserRole.USER);
        User registeredUser = userService.registerUser(registerDTO);

        if (registeredUser.getId() != 0) {
            response.put("message", "user registered");
        } else {
            response.put("message", "user wasn't registered");
        }

        return ResponseEntity.ok(response);
    }
}
