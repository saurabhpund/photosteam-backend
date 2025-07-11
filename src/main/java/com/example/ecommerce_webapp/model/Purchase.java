package com.example.ecommerce_webapp.model;

import com.example.ecommerce_webapp.enums.PurchasedStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


    @Entity
    @Table(name = "purchases")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Purchase {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Buyer of the image
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "buyer_id", nullable = false)
        private User buyer;

        // Purchased image
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "image_id", nullable = false)
        private Image image;

        private LocalDateTime purchasedAt = LocalDateTime.now();

        private String paymentReference;

        @Enumerated(EnumType.STRING)
        private PurchasedStatus status = PurchasedStatus.SUCCESS;
    }
