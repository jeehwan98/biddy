package com.jee.back.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {

    private String userId;
    private String password;
}
