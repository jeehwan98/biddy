package com.jee.back.user.service;

import com.jee.back.auth.dto.RegisterDTO;
import com.jee.back.user.entity.User;
import com.jee.back.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public void registerUser(RegisterDTO registerDTO) {
        User registerUserInfo = modelMapper.map(registerDTO, User.class);
        userRepository.save(registerUserInfo);
    }

}
