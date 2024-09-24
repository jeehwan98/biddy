package com.jee.biddy1.user;

import com.jee.biddy1.user.entity.User;
import com.jee.biddy1.user.entity.UserRole;
import com.jee.biddy1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("UserDetailsService inputted userID: " + userId);
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("user not found with userId: " + userId);
        }

        User user = userOptional.get();
        Collection<? extends GrantedAuthority> authorities = mapAuthorities(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getPassword(),
                mapAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> mapAuthorities(UserRole role) {
        return role.getAuthorities();
    }
}
