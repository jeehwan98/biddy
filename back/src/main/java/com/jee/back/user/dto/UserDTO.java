package com.jee.back.user.dto;

import com.jee.back.bid.dto.ProductDTO;
import com.jee.back.common.UserRole;
import lombok.*;

import java.util.List;

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
    private String userStatus;
    @ToString.Exclude
    private List<ProductDTO> products;

}
