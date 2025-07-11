package com.example.ecommerce_webapp.service;

import com.example.ecommerce_webapp.model.User;
import com.example.ecommerce_webapp.repository.UserRepository;
import com.example.ecommerce_webapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new user
    public Optional<User> registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return Optional.empty();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return  Optional.of(userRepository.save(user));
    }

    // Authenticate user and return JWT token
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail());
    }

    // Load user by email (utility method)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
