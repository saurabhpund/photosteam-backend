package com.example.ecommerce_webapp.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "download_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadLog {
    @Id
    long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    private LocalDateTime downloadedAt = LocalDateTime.now();
}
