package com.jee.back.bid.service;

import com.jee.back.bid.dto.ProductDTO;
import com.jee.back.bid.entity.Product;
import com.jee.back.bid.repository.BidRepository;
import com.jee.back.bid.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public Product uploadProduct(ProductDTO productDTO) {
        Product productToUpload = modelMapper.map(productDTO, Product.class);
        log.info("product to upload: " + productToUpload);
        Product uploadedProduct = productRepository.save(productToUpload);
        return uploadedProduct;
    }
}
