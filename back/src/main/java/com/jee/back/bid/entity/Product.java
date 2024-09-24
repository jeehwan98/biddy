package com.jee.back.bid.entity;

import com.jee.back.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "starting_price", nullable = false)
    private int startingPrice;

    @Column(name = "current_price", nullable = false)
    private int currentPrice;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "bid_start_time", nullable = false)
    private LocalDateTime bidStartTime;

    @Column(name = "bid_end_time", nullable = false)
    private LocalDateTime bidEndTime;

    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Bid> bids;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
