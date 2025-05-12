// File: FriendshipServiceImpl.java
package com.splitmate.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import com.splitmate.model.FilterType;
import org.springframework.stereotype.Service;

import com.splitmate.model.Currency;
import com.splitmate.model.Friendship;
import com.splitmate.model.Group;
import com.splitmate.model.Notification;
import com.splitmate.model.NotificationType;
import com.splitmate.model.Payment;
import com.splitmate.model.Transaction;
import com.splitmate.model.User;
import com.splitmate.repository.FriendshipRepository;
import com.splitmate.repository.UserRepository;

import main.java.com.splitmate.service.UserService;

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
    private final GroupService groupService;
    @Autowired private PaymentCalculator paymentCalculator;

    public FriendshipServiceImpl(FriendshipRepository friendshipRepo,
                                 UserRepository userRepo,
                                 NotificationService notificationService,
                                 GroupService groupService) {
        this.friendshipRepo = friendshipRepo;
        this.userRepo       = userRepo;
        this.notificationService = notificationService;
        this.groupService = groupService;
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
    public List<Transaction> sortByAmountAsc(Friendship friendship) {
        List<Transaction> history = new ArrayList<>(friendship.getHistory());
        history.sort(Comparator.comparing(Transaction::getAmount));
        return history;
    }

    // from maximum to minimum
    @Override
    public List<Transaction> sortByAmountDesc(Friendship friendship) {
        List<Transaction> history = new ArrayList<>(friendship.getHistory());
        history.sort(Comparator.comparing(Transaction::getAmount).reversed());
        return history;
    }

    // sorts from newest to oldest
    @Override
    public List<Transaction> sortByDateDesc(Friendship friendship) {
        List<Transaction> history = new ArrayList<>(friendship.getHistory());
        history.sort(Comparator.comparing(Transaction::getPaymentDate).reversed());
        return history;
    }

    @Override
    public Friendship getFriendshipById(String friendshipId) {
        return friendshipRepo.findById(friendshipId)
        .orElseThrow(() -> new NoSuchElementException("Friendship not found with ID: " + friendshipId));
    }

    @Override
    public Friendship getFriendshipBetween(String userAId, String userBId) {
        User a = userRepo.findById(userAId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userAId));
        User b = userRepo.findById(userBId)
            .orElseThrow(() -> new NoSuchElementException("User not found: " + userBId));

        return friendshipRepo.findByUserAOrUserB(a, b)
            .stream()
            .filter(f ->
                (f.getUserA().equals(a) && f.getUserB().equals(b)) ||
                (f.getUserA().equals(b) && f.getUserB().equals(a))
            )
            .findFirst()
            .orElse(null);
    }

    @Override
    public void friendAllInGroup(String groupId) {
    // 1) Load the group and its members
        Group group = groupService.getGroup(groupId);
        List<User> members = group.getMembers();

        // 2) For each unordered pair (i,j) in members
        for (int i = 0; i < members.size(); i++) {
            User a = members.get(i);
            for (int j = i + 1; j < members.size(); j++) {
                User b = members.get(j);

                boolean alreadyFriends = false;
                
                // 3) Skip if already friends
                List<User> friends = a.getFriends();
                if(friends.contains(b)){
                    alreadyFriends = true;
                }
                if (alreadyFriends) continue;

               // 4) Persist the friendship
                Friendship f = new Friendship();
                f.setUserA(a);
                f.setUserB(b);
                friendshipRepo.save(f);

                // 5) Add to each user's list and save
                a.addFriend(b);
                b.addFriend(a);
                userRepo.save(a);
                userRepo.save(b);
            }
        }
    }

    @Override
    public List<Transaction> filterByDateRange(List<Transaction> transactions,
                                               LocalDate start,
                                               LocalDate end) {
      return transactions.stream()                             
        .filter(tx -> {
            LocalDate txDate = tx.getPaymentDate().toLocalDate();
            boolean afterOrEqStart = (start == null) || !txDate.isBefore(start);
            boolean beforeOrEqEnd  = (end   == null) || !txDate.isAfter(end);
            return afterOrEqStart && beforeOrEqEnd;
        })
        .collect(Collectors.toList());
    }
    
    @Override
    public List<Transaction> filterByAmountRange(List<Transaction> transactions,
                                                 BigDecimal min,
                                                 BigDecimal max) {
        return transactions.stream()
            .filter(tx -> {
                BigDecimal amt = tx.getAmount();
                return (min == null || amt.compareTo(min) >= 0)
                    && (max == null || amt.compareTo(max) <= 0);
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> filterByCurrency(List<Transaction> transactions, Currency currency){
        transactions.removeIf(tx -> !tx.getCurrency().equals(currency));
        return transactions;
    }

    @Override
    public List<Transaction> filterByType(Friendship f,
                                          String currentUserId,
                                          FilterType type) {

         return f.getHistory().stream()
        .filter(tx -> {
            // if “from” is current user → payment; otherwise → receivement
            boolean isPayment = tx.getFrom().getId().equals(currentUserId);

            switch (type) {
                case PAYMENTS:
                    return isPayment;
                case RECEIVEMENTS:
                    return !isPayment;
                case BOTH:
                    return true;
                default:
                    return false;
            }
        })
        .collect(Collectors.toList());
    }

    @Override
    public void addFriendshipDebt(String payerId, String friendId, BigDecimal payerSplit, BigDecimal friendSplit, Currency currency) {
        User payer = userRepo.findById(payerId)
            .orElseThrow(() -> new NoSuchElementException("Payer not found: " + payerId));
        User friend = userRepo.findById(friendId)
            .orElseThrow(() -> new NoSuchElementException("Friend not found: " + friendId));

        
        // directly edit balances of the users
        payer.getBalanceByCurrency(currency).addAmount(payerSplit.add(friendSplit)); // add the total amount to payer
        payer.getBalanceByCurrency(currency).addAmount(payerSplit.negate()); // subtract the payer's split
        friend.getBalanceByCurrency(currency).addAmount(friendSplit.negate()); // subtract the friend's split

        userRepo.save(payer);
        userRepo.save(friend);

        // run the main algorithm to calculate the debts
        paymentCalculator.calculate();

    }
}