package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;

public interface UserService {
    User register(User user);
    
    User findByEmail(String email);
    
    User getById(Long id);
    
    List<User> getAllUsers();

    // FIX: Add this method so UserServiceImpl can successfully override it
    void deleteUser(Long id);
}