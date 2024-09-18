package com.jee.back.user.dto;

import com.jee.back.common.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private int id;
    private String userId;
    private String name;
    private String password;
    private String email;
    private UserRole role;
    private String imageUrl;

}
