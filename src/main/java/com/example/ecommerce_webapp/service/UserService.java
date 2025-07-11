package com.example.ecommerce_webapp.service;

import com.example.ecommerce_webapp.model.User;
import com.example.ecommerce_webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User getUserByFullName(String name) {
        return userRepository.findByFullName(name);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
