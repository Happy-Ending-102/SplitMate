// File: UserController.java
package com.splitmate.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.splitmate.model.Frequency;
import com.splitmate.model.Group;
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

    public User findUserByEmail(String email) {
        return userService.findByEmail(email.trim().toLowerCase());
    }
    

    /**
     * Lookup an existing user.
     */
    public User findUserById(String id) {
        return userService.getUser(id);
    }

    /**
     * Return user's groups
     */
    public List<Group> getGroupsOfUser(String userId){
        return userService.getGroupsOfUser(userId);
    }

    public boolean resetPassword(String email){
        return userService.resetPassword(email);
    }

    public void updateFrequency(String userId, Frequency frequency){
        userService.getUser(userId).setFrequency(frequency);
    }
}
