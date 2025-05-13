package com.splitmate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.splitmate.repository.GroupRepository;

/**
 * Holds the currently logged-in user for the application session.
 */
@Component
public class SessionService {
    private User currentUser;
    private Group currentGroup;
    private User currentFriend;
    @Autowired private UserService userService;
    @Autowired private GroupRepository groupRepo;

    /**
     * Get the full User object of the currently logged-in user.
     */
    public User getCurrentUser() {
        return userService.getUser(getCurrentUserId());
    }

    /**
     * Set the User object for the current session.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Get the ID of the current user, or null if not set.
     */
    public String getCurrentUserId() {
        return (currentUser != null) ? currentUser.getId() : null;
    }

    /**
     * Clear the current session (logout).
     */
    public void clear() {
        this.currentUser = null;
    }

    public Group getCurrentGroup() {
        return groupRepo.findById(getCurrentGroupId()).orElse(null);
    }

    public void setCurrentGroup(Group group) {
        this.currentGroup = group;
    }
    public String getCurrentGroupId() {
        return (currentGroup != null) ? currentGroup.getId() : null;
    }
    public void clearGroup() {
        this.currentGroup = null;
    }

    
    public User getCurrentFriend() {
        return userService.getUser(getCurrentFriendId());
    }

    public void setCurrentFriend(User friend) {
        this.currentFriend = friend;
    }

    public void clearFriend() {
        this.currentFriend = null;
    }

    public String getCurrentFriendId() {
        return currentFriend != null ? currentFriend.getId() : null;
    }
    
  
}
