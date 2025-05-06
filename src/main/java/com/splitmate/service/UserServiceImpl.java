package com.splitmate.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.splitmate.model.Balance;
import com.splitmate.model.User;
import com.splitmate.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String id) {
        return userRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    @Override
    public User registerUser(User u, String rawPassword) {
        // hash & set the password
        u.setPasswordHash(passwordEncoder.encode(rawPassword));
        // initialize balance if absent
        if (u.getBalances() == null) {
            u.setBalances( new ArrayList<>());
        }
        return userRepo.save(u);
    }

    @Override
    public boolean authenticate(String email, String rawPassword) {
        // assume your UserRepository.findByEmail returns null when not found
        User found = userRepo.findByEmail(email);
        return found != null && passwordEncoder.matches(rawPassword, found.getPasswordHash());
    }

    @Override
    public User updateUser(User u) {
        return userRepo.save(u);
    }

    @Override
    public void removeUser(String id) {
        userRepo.deleteById(id);
    }

    @Override
    public String getAvatarBase64(String userId) {
        User u = getUser(userId);
        return u.getAvatarBase64();  // may be null or empty
    }

    @Override
    public User updateAvatar(String userId, String base64) {
        User u = getUser(userId);
        u.setAvatarBase64(base64);
        return userRepo.save(u);
    }
}
