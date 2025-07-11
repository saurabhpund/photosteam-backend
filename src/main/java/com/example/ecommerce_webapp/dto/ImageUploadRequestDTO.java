package com.example.ecommerce_webapp.dto;

import com.example.ecommerce_webapp.enums.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class    ImageUploadRequestDTO {
    private String title;
    private String description;
    private String tags;
    private MultipartFile file;
    private boolean isPublicImage;
    private BigDecimal price = BigDecimal.ZERO;
    private Category category;
}