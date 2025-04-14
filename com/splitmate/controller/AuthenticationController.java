package com.splitmate.controller;

import com.splitmate.model.User;
import com.splitmate.repository.MongoUserRepository;

public class AuthenticationController {
    private MongoUserRepository userRepo;
    
    public AuthenticationController() {
        userRepo = new MongoUserRepository();
    }
    
    public boolean login(String email, String password) {
        for (User user : userRepo.findAll()) {
            if (user.getEmail().equals(email) && user.authenticate(password)) {
                // Perform session initialization or other login actions.
                return true;
            }
        }
        return false;
    }
    
    public boolean signUp(String name, String email, String password, String iban) {
        User newUser = new User(name, email, password, iban);
        return userRepo.insert(newUser);
    }
}
