package com.jee.back.user.controller;

import com.jee.back.common.AuthenticatedUser;
import com.jee.back.user.entity.User;
import com.jee.back.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUsers() {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/user")
    public ResponseEntity<?> fetchCurrentUser() {
        log.info("fetch current user???");
        User user = AuthenticatedUser.fetchUserInfo();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> fetchUserByUserId(@PathVariable("userId") String userId) {
        User user = (userRepository.findByUserId(userId)).get();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userId + " could not be found");
        }
        return ResponseEntity.ok(user);
    }

}
