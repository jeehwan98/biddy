package com.jee.back.auth.service;

import com.jee.back.auth.DetailsUser;
import com.jee.back.user.entity.User;
import com.jee.back.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 1
@Service
public class DetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        if (userId == null) {
            throw new AuthenticationServiceException("userId: " + userId + " is invalid");
        }

//        Optional<User> user = userRepository.findByUserId(userId);


        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException("no user found for: " + userId);
        }
        return new DetailsUser(user);
    }
}
