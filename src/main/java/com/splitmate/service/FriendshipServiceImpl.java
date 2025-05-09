// File: FriendshipServiceImpl.java
package com.splitmate.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.splitmate.model.Friendship;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;
import com.splitmate.model.User;
import com.splitmate.repository.FriendshipRepository;
import com.splitmate.repository.UserRepository;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Service
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;

    public FriendshipServiceImpl(FriendshipRepository friendshipRepo,
                                 UserRepository userRepo,
                                 NotificationService notificationService) {
        this.friendshipRepo = friendshipRepo;
        this.userRepo       = userRepo;
        this.notificationService = notificationService;
    }

    @Override
    public void acceptFriendRequest(String requesterId, String recipientId) {
        // 1) Load both users
        User requester = userRepo.findById(requesterId)
            .orElseThrow(() -> new NoSuchElementException("Requester not found: " + requesterId));
        User recipient = userRepo.findById(recipientId)
            .orElseThrow(() -> new NoSuchElementException("Recipient not found: " + recipientId));

        // 2) Persist the friendship
        Friendship f = new Friendship();
        f.setUserA(requester);
        f.setUserB(recipient);
        friendshipRepo.save(f);

        // 3) Add to each user's list and save
        requester.addFriend(recipient);
        recipient.addFriend(requester);
        userRepo.save(requester);
        userRepo.save(recipient);

    }

    @Override
    public void sendFriendRequest(String requesterId, String recipientId) {
        // 1) Load both users
        User requester = userRepo.findById(requesterId)
            .orElseThrow(() -> new NoSuchElementException("Requester not found: " + requesterId));
        User recipient = userRepo.findById(recipientId)
            .orElseThrow(() -> new NoSuchElementException("Recipient not found: " + recipientId));

        Notification notification = new Notification();

        notification.setUser(recipient);
        notification.setType(NotificationType.FRIEND_REQUEST);
        notification.setMessage(requester.getName() + " sent you a friend request.");
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setFriendUser(requester);

        notificationService.createNotification(notification);
    }

    @Override
    public List<User> getFriends(String userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        return user.getFriends();
    }
}