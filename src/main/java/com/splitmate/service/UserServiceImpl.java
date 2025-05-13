package com.splitmate.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.splitmate.model.Frequency;
import com.splitmate.model.Friendship;
import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.splitmate.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender    mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepo,
                           PasswordEncoder passwordEncoder,
                           JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender     = mailSender;
    }

    @Override
    public User getUser(String id) {
        return userRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    @Override
    public User registerUser(User u, String rawPassword) {
        // Normalize email
        String email = u.getEmail().trim().toLowerCase();

        // Pre-check: email already in use?
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("That email is already registered.");
        }

        // Prepare user entity
        u.setEmail(email);
        u.setPasswordHash(passwordEncoder.encode(rawPassword));
        if (u.getBalances() == null) {
            u.setBalances(new ArrayList<>());
        }

        // Save, with catch for rare race conditions
        try {
            return userRepo.save(u);
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("That email is already registered.");
        }
    }

    @Override
    public boolean authenticate(String email, String rawPassword) {
        // Normalize email for lookup
        String normalized = email.trim().toLowerCase();
        User found = userRepo.findByEmail(normalized);
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
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
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

    @Override
    public List<Group> getGroupsOfUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        return user.getGroups();
    }

    @Override
    public List<User> getFriendsOfUser(String userId) {
        // reload from MongoDB
        User fresh = userRepo.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        return fresh.getFriends();
    }

    @Override
    public boolean resetPassword(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) return false;

        String newPwd = generateRandomPassword(10);
        user.setPasswordHash(passwordEncoder.encode(newPwd));
        userRepo.save(user);
        sendResetEmail(email, newPwd);
        return true;
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                     + "abcdefghijklmnopqrstuvwxyz"
                     + "0123456789";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void sendResetEmail(String email, String newPassword) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("SplitMate: Your New Password");
        msg.setText(
            "Hello,\n\n" +
            "Your password has been reset. Your new password is:\n\n" +
            "    " + newPassword + "\n\n" +
            "Please log in using this password.\n\n" +
            "— The SplitMate Team"
        );
        mailSender.send(msg);
    }

    @Override
    public void updateFrequency(String userId, Frequency frequency) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NoSuchElementException("User with: " + userId + " not find"));
        user.setFrequency(frequency);
        userRepo.save(user);
    }
}
