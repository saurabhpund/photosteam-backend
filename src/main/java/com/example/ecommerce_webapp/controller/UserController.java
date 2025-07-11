package com.example.ecommerce_webapp.controller;

import com.example.ecommerce_webapp.dto.UserResponseDTO;
import com.example.ecommerce_webapp.model.User;
import com.example.ecommerce_webapp.service.UserService;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Get current logged-in user's profile
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
        User user = userService.getUserByFullName(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(Map.of("error", "User not found: " + principal.getName()));
        }
        return ResponseEntity.ok(new UserResponseDTO()
                .builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
        );
    }

    // ✅ Get all users (admin access)
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userDTOs = users.stream().map(UserResponseDTO::new).toList();
        return ResponseEntity.ok(userDTOs);
    }

    // ✅ Delete user by ID (admin access)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("success", "User deleted successfully"));
    }

    // ✅ Update user details (optional feature)
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser, Principal principal) {
        User user = userService.getUserByFullName(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        // optionally update other fields

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(new UserResponseDTO(savedUser));
    }
}
