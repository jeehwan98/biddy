package com.jee.back.bid.controller;

import com.jee.back.bid.dto.ProductDTO;
import com.jee.back.bid.entity.Product;
import com.jee.back.bid.repository.BidRepository;
import com.jee.back.bid.repository.ProductRepository;
import com.jee.back.bid.service.BidService;
import com.jee.back.common.AuthenticatedUser;
import com.jee.back.user.dto.UserDTO;
import com.jee.back.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BidController {

    private final BidService bidService;
    private final BidRepository bidRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @PostMapping("/post/product")
    public ResponseEntity<?> uploadProduct(@RequestBody ProductDTO productDTO) {
        int startingPrice = productDTO.getStartingPrice();
        productDTO.setCurrentPrice(startingPrice);
        productDTO.setBidStartTime(LocalDateTime.now());
        User user = AuthenticatedUser.fetchUserInfo();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        productDTO.setUserDTO(userDTO);
        Product uploadedProduct = bidService.uploadProduct(productDTO);
        return ResponseEntity.ok(uploadedProduct);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> fetchAllProducts() {
        List<Product> fetchAllProducts = productRepository.findAll();
        log.info("all products: " + fetchAllProducts);
        if (!fetchAllProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fetchAllProducts);
        }

        return ResponseEntity.ok(fetchAllProducts);
    }

}
