// File: UserService.java
package com.splitmate.service;

import com.splitmate.model.User;

public interface UserService {
    User getUser(String id);
    User registerUser(User u, String rawPassword);
    boolean authenticate(String email, String rawPassword);
    User updateUser(User u);
    void removeUser(String id);
    String getAvatarBase64(String userId);
    User   updateAvatar(String userId, String base64);
}
