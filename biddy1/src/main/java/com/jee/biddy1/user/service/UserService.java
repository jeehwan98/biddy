package com.jee.biddy1.user.service;

import com.jee.biddy1.auth.dto.RegisterDTO;
import com.jee.biddy1.user.entity.User;
import com.jee.biddy1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public User findByUserId(String userId) {
        Optional<User> foundUserByUserId = userRepository.findByUserId(userId);
        if (foundUserByUserId.isPresent()) {
            User user = foundUserByUserId.get();
            return user;
        } else {
            throw new UsernameNotFoundException(userId);
        }
    }

    public User registerUser(RegisterDTO registerDTO) {
        User user = modelMapper.map(registerDTO, User.class);
        User registeredUser = userRepository.save(user);
        if (registeredUser != null) {
            return registeredUser;
        } else {
            return null;
        }
    }
}
