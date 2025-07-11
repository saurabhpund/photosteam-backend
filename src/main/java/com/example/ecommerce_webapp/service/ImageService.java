package com.example.ecommerce_webapp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce_webapp.dto.ImageUploadRequestDTO;
import com.example.ecommerce_webapp.model.Image;
import com.example.ecommerce_webapp.model.User;
import com.example.ecommerce_webapp.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(Cloudinary cloudinary, ImageRepository imageRepository) {
        this.cloudinary = cloudinary;
        this.imageRepository = imageRepository;
    }

    public Image uploadImage(ImageUploadRequestDTO dto, User currentUser) {
        MultipartFile file = dto.getFile();
        try {
            Map<String, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "image-gallery")
            );

            String imageUrl = (String) uploadResult.get("secure_url");
            String format = (String) uploadResult.get("format");
            int width = (int) uploadResult.get("width");
            int height = (int) uploadResult.get("height");

            if (!dto.isPublicImage()) {
                dto.setPrice(BigDecimal.ZERO);
            } else if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Premium images must have a valid price.");
            }

            Image image = Image.builder()
                    .title(dto.getTitle())
                    .description(dto.getDescription())
                    .tags(dto.getTags())
                    .imagePath(imageUrl)
                    .format(format)
                    .resolution(width + "x" + height)
                    .publicImage(dto.isPublicImage())
                    .price(dto.getPrice())
                    .category(dto.getCategory())
                    .uploadedBy(currentUser)
                    .build();

            return imageRepository.save(image);

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }

    public Page<Image> findAllImages(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    public void deleteImage(Long id) {
        try {
            Optional<Image> imageOptional = imageRepository.findById(id);
            if (imageOptional.isPresent()) {
                cloudinary.uploader().destroy(id.toString(), ObjectUtils.emptyMap());
                imageRepository.deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existImage(Long id) {
        return imageRepository.existsById(id);
    }

    public Page<Image> getPublicImages(Pageable pageable) {
        return imageRepository.findByPublicImageFalse(pageable);
    }

    public Page<Image> getImageByUser(Pageable pageable, User user) {
        return imageRepository.findByUploadedBy(pageable, user);
    }

    // ✅ NEW: Get image by ID
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    // ✅ NEW: Update image details
    public Optional<Image> updateImage(Long id, ImageUploadRequestDTO dto, User user) {
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (optionalImage.isEmpty()) return Optional.empty();

        Image image = optionalImage.get();

        // Optional: Validate that this user is the uploader
        if (image.getUploadedBy().getId() != user.getId()) {
            return Optional.empty(); // Unauthorized update
        }

        image.setTitle(dto.getTitle());
        image.setDescription(dto.getDescription());
        image.setTags(dto.getTags());
        image.setCategory(dto.getCategory());
        image.setPublicImage(dto.isPublicImage());

        if (!dto.isPublicImage()) {
            image.setPrice(BigDecimal.ZERO);
        } else if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Premium image must have a valid price.");
        } else {
            image.setPrice(dto.getPrice());
        }

        return Optional.of(imageRepository.save(image));
    }


    public Page<Image> searchImages(String query, Pageable pageable) {
        return imageRepository.findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCase(query, query, pageable);
    }
}
