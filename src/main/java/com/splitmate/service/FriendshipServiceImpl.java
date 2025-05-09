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
        f.setSince(LocalDate.now());
        friendshipRepo.save(f);

        // 3) Add to each user's list and save
        requester.addFriend(recipient);
        recipient.addFriend(requester);
        userRepo.save(requester);
        userRepo.save(recipient);

        // 4) Notify the requester
        notificationService.createNotification(
            requesterId,
            NotificationType.FRIEND_REQUEST,
            recipient.getName() + " accepted your friend request."
        );
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
        notification.setCreatedAt(LocalDate.now());
        notification.setFriendUser(requester);

        notificationService.createNotification(notification);
    }
}