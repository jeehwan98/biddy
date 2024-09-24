package com.jee.back.bid.dto;

import com.jee.back.user.dto.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

    private int id;
    private String productId;
    private String productName;
    private String description;
    private int startingPrice;
    private int currentPrice;
    private String imageUrl;
    private LocalDateTime bidStartTime;
    private LocalDateTime bidEndTime;
    @ToString.Exclude
    private List<BidDTO> bidsDTO;
    private UserDTO userDTO;
}
