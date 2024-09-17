package com.jee.back.user.entity;

import com.jee.back.common.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "user_id", nullable = false, unique = true, length = 30)
    private String userId;

    @Column(name = "name", nullable = false, unique = false, length = 30)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 90)
    private String email;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "user_status", nullable = false, length = 30)
    private String userStatus;
}
