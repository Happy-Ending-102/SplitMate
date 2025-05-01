// File: UserController.java
package com.splitmate.controller;

import org.springframework.stereotype.Component;

import com.splitmate.model.User;
import com.splitmate.service.UserService;

@Component
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user with raw password.
     */
    public User registerUser(User dto, String rawPassword) {
        return userService.registerUser(dto, rawPassword);
    }

    /**
     * Authenticate (login) by email and password.
     */
    public boolean login(String email, String rawPassword) {
        return userService.authenticate(email, rawPassword);
    }

    /**
     * Lookup an existing user.
     */
    public User findUserById(String id) {
        return userService.getUser(id);
    }
}
