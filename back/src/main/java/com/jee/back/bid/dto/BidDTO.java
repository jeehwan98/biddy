package com.jee.back.bid.dto;

import com.jee.back.bid.entity.Product;
import com.jee.back.user.dto.UserDTO;
import com.jee.back.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BidDTO {

    private int id;
    @ToString.Exclude
    private ProductDTO productDTO;
    private UserDTO userDTO;
    private int bidPrice;
    private LocalDateTime bidTime;
}
