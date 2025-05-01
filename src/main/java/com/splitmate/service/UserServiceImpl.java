// File: UserServiceImpl.java
package com.splitmate.service;

import org.springframework.stereotype.Service;
import com.splitmate.repository.UserRepository;
import com.splitmate.model.User;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final CurrencyConverter converter;

    public UserServiceImpl(UserRepository userRepo,
                           CurrencyConverter converter) {
        this.userRepo = userRepo;
        this.converter = converter;
    }

    @Override
    public User getUser(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User registerUser(User u, String rawPassword) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean authenticate(String email, String rawPassword) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User updateUser(User u) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeUser(String id) {
        throw new UnsupportedOperationException();
    }
}
