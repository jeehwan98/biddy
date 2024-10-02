package com.jee.biddy1.user.dto;

import com.jee.biddy1.user.entity.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private int id;
    private String userId;
    private String password;
    private String imageUrl;
    private UserRole role;
    private LocalDateTime createdDate;
}
