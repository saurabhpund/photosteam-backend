package com.example.ecommerce_webapp.controller;

import com.example.ecommerce_webapp.model.User;
import com.example.ecommerce_webapp.service.AuthService;
import com.example.ecommerce_webapp.service.CustomUserDetailsService;
import com.example.ecommerce_webapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Optional<User> createdUser = authService.registerUser(user);
        if(createdUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "User with this email already exist"));
        }
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            String token = authService.login(email, password);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "error", "Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            boolean valid = jwtUtil.isTokenValid(token, userDetails);

            return ResponseEntity.ok(Map.of("valid", valid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "error", e.getMessage()));
        }
    }
}
