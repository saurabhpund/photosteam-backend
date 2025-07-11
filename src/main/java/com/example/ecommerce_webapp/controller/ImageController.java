package com.example.ecommerce_webapp.controller;

import com.example.ecommerce_webapp.dto.ImageResponseDTO;
import com.example.ecommerce_webapp.dto.ImageUploadRequestDTO;
import com.example.ecommerce_webapp.model.Image;
import com.example.ecommerce_webapp.model.User;
import com.example.ecommerce_webapp.service.ImageService;
import com.example.ecommerce_webapp.service.UserService;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@ModelAttribute ImageUploadRequestDTO dto, Principal principal){
        User user = userService.getUserByEmail(principal.getName());

        if(user == null){
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(Map.of("error", principal.getName() + "not found"));
        }
        Image image = imageService.uploadImage(dto, user);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        Optional<Image> image = imageService.findById(id);

        if (image.isPresent()) {
            return ResponseEntity.ok(new ImageResponseDTO(image.get()));
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(Map.of("error", "Image with ID " + id + " not found"));
        }
    }


    @GetMapping("/all")
    public Page<Image> getImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return imageService.findAllImages(pageable);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(@RequestParam Long id){
        if(imageService.existImage(id)){
            imageService.deleteImage(id);
            return ResponseEntity.status(HttpStatus.SC_OK).body(Map.of("Success", "Image deleted Successfully"));
        }

        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(Map.of("Error", "Image not found"));
    }

    @GetMapping("/public")
    public Page<Image> getFreeImages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return imageService.getPublicImages(PageRequest.of(page, size));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getImagesByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ){
        User user = userService.getUserByFullName(principal.getName());
        if(user == null){
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(Map.of("Error", "User not found" + principal.getName()));
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Image> image = imageService.getImageByUser(pageable, user);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/search")
    public Page<ImageResponseDTO> searchImages(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return imageService.searchImages(query, pageable)
                .map(ImageResponseDTO::new);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateImage(
            @PathVariable Long id,
            @RequestBody ImageUploadRequestDTO dto,
            Principal principal
    ) {
        User user = userService.getUserByFullName(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        Optional<Image> updated = imageService.updateImage(id, dto, user);

        if (updated.isPresent()) {
            return ResponseEntity.ok(new ImageResponseDTO(updated.get()));
        } else {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(Map.of("error", "Image not found or unauthorized"));
        }
    }
}
