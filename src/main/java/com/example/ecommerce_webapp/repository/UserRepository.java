package com.example.ecommerce_webapp.repository;

import com.example.ecommerce_webapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFullName(String name);

    User findByEmail(String email);

    boolean existsByEmail(String email);

}
