package com.example.ecommerce_webapp.model;

import com.example.ecommerce_webapp.enums.Category;
import com.example.ecommerce_webapp.enums.ImageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column
    String imagePath;
    private String resolution;

    @Column
    private String tags;

    @Column
    private String format;

    @Column
    private boolean publicImage;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column
    private Category category;

    @Enumerated(EnumType.STRING)
    private ImageStatus status = ImageStatus.PENDING;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;



    @PrePersist
    public void setDefaults() {
        if (price == null) price = BigDecimal.ZERO;
        if (status == null) status = ImageStatus.PENDING;
        if (uploadedAt == null) uploadedAt = LocalDateTime.now();
    }

}
