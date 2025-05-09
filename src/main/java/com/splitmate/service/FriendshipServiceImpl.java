// File: FriendshipServiceImpl.java
package com.splitmate.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.splitmate.model.Friendship;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;
import com.splitmate.model.Payment;
import com.splitmate.model.User;
import com.splitmate.repository.FriendshipRepository;
import com.splitmate.repository.UserRepository;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import java.util.Comparator;
import java.util.Collections;

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

        System.out.println("Sending friend request notification: " + notification.getUser().getName());

        notificationService.createNotification(notification);

        recipient.addNotification(notification);
        userRepo.save(recipient);
    }

    @Override
    public List<User> getFriends(String userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));
        return user.getFriends();
    }

    // from minimum to maximum
    @Override
    public List<Payment> sortByAmountAsc(Friendship friendship) {
        List<Payment> history = new ArrayList<>(friendship.getHistory());
        history.sort(Comparator.comparing(Payment::getAmount));
        return history;
    }

    // from maximum to minimum
    @Override
    public List<Payment> sortByAmountDesc(Friendship friendship) {
        List<Payment> history = new ArrayList<>(friendship.getHistory());
        history.sort(Comparator.comparing(Payment::getAmount).reversed());
        return history;
    }

    // sorts from newest to oldest
    @Override
    public List<Payment> sortByDateDesc(Friendship friendship) {
        List<Payment> history = new ArrayList<>(friendship.getHistory());
        history.sort(Comparator.comparing(Payment::getPaymentDate).reversed());
        return history;
    }

    @Override
    public Friendship getFriendshipById(String friendshipId) {
        return friendshipRepo.findById(friendshipId)
        .orElseThrow(() -> new NoSuchElementException("Friendship not found with ID: " + friendshipId));
    }
}