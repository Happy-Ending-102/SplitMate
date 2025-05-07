package com.splitmate.service;

import org.springframework.stereotype.Component;
import com.splitmate.model.User;

/**
 * Holds the currently logged-in user for the application session.
 */
@Component
public class SessionService {
    private User currentUser;

    /**
     * Get the full User object of the currently logged-in user.
     */
    public User getCurrentUser() {
        return currentUser;
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
}
