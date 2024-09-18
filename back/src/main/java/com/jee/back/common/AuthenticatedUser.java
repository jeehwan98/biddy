package com.jee.back.common;

import com.jee.back.auth.DetailsUser;
import com.jee.back.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUser {

    public static User fetchUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DetailsUser detailsUser = (DetailsUser) authentication.getPrincipal();
        User user = detailsUser.getUser();

        if (user == null) {
            return null;
        }

        return user;
    }
}
