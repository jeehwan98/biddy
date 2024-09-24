package com.jee.biddy1.auth.dto;

import com.jee.biddy1.user.entity.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterDTO {

    private int id;
    private String userId;
    private String username;
    private String password;
    private String imageUrl;
    private UserRole role;
}
