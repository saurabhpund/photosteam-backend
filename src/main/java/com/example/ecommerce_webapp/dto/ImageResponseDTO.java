package com.example.ecommerce_webapp.dto;


import com.example.ecommerce_webapp.model.Image;
import com.example.ecommerce_webapp.model.User;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ImageResponseDTO {
        private Long id;
        private String title;
        private String description;
        private String tags;
        private String imageUrl;
        private String resolution;
        private String format;
        private String uploadedBy;
        private LocalDateTime uploadedAt;
        private boolean isPublicImage;
        private BigDecimal price = BigDecimal.ZERO;

        public ImageResponseDTO(Image image){
             this.id = image.getId();
             this.title = image.getTitle();
             this.description = image.getDescription();
             this.tags = image.getTags();
             this.imageUrl = image.getImagePath();
             this.resolution = image.getResolution();
             this.format = image.getFormat();
             this.uploadedBy = image.getUploadedBy().getEmail();
             this.uploadedAt = image.getUploadedAt();
             this.isPublicImage = image.isPublicImage();
        }

    }

