package com.jee.back.auth.dto;

import com.jee.back.common.UserRole;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterDTO {

    private int id;
    private String name;
    private String userId;
    private String password;
    private String email;
    private String imageUrl;
    private String userStatus;
    private UserRole role;
}
