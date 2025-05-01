// UserController.java
package com.splitmate.controller;

import com.splitmate.model.*;
import com.splitmate.service.*;
import org.springframework.stereotype.Component;

/**
 * Handles user-related UI actions (e.g. from Swing forms).
 */
@Component
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User registerUser(User dto, String rawPassword) {
        throw new UnsupportedOperationException();
    }

    public boolean login(String email, String rawPassword) {
        throw new UnsupportedOperationException();
    }

    public User findUserById(String id) {
        throw new UnsupportedOperationException();
    }
}