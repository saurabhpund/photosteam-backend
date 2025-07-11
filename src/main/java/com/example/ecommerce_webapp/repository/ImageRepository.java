package com.example.ecommerce_webapp.repository;

import com.example.ecommerce_webapp.model.Image;
import com.example.ecommerce_webapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByTitleContainingIgnoreCase(String title);

    List<Image> findByTagsContainingIgnoreCase(String tag);

    Page<Image> findByUploadedBy(Pageable pageable, User user);

    List<Image> findByPublicImageFalse();

    Page<Image> findByPublicImageFalse(Pageable pageable);

    // For search
    Page<Image> findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCase(String title, String tags, Pageable pageable);
}
