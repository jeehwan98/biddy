package com.jee.biddy1.user.controller;

import com.jee.biddy1.user.entity.User;
import com.jee.biddy1.user.repository.UserRepository;
import com.jee.biddy1.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /** all users */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return ResponseEntity.ok(allUsers);
    }

    /** find specific user */
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> findUser(@RequestParam("userId") String userId) {
        User findUserById = userService.findByUserId(userId);
        if (findUserById != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(findUserById);
        }
        return ResponseEntity.ok(findUserById);
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Hello, admin";
    }
}
