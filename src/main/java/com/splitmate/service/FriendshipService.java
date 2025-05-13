// File: FriendshipService.java
package com.splitmate.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.splitmate.model.FilterType;

import com.splitmate.model.Currency;
import com.splitmate.model.Friendship;
import com.splitmate.model.Payment;
import com.splitmate.model.Transaction;
import com.splitmate.model.User;
import com.splitmate.model.Partition;
import com.splitmate.model.Group;
import com.splitmate.model.Currency;

public interface FriendshipService {
    /**
     * Called by the recipient to accept a pending friend request.
     *
     * @param requesterId the ID of the user who sent the request
     * @param recipientId the ID of the user accepting it
     */
    void acceptFriendRequest(String requesterId, String recipientId);
    void sendFriendRequest(String requesterId, String recipientId);
    List<User> getFriends(String userId);
    List<Transaction> sortByAmountAsc(Friendship friendship);
    List<Transaction> sortByAmountDesc(Friendship friendship);
    List<Transaction> sortByDateDesc(Friendship friendship);
    Friendship getFriendshipById(String friendshipId);
    Friendship getFriendshipBetween(String userAId, String userBId);
    List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDate start, LocalDate end);
    List<Transaction> filterByAmountRange(List<Transaction> transactions, BigDecimal minAmount, BigDecimal maxAmount);
    List<Transaction> filterByCurrency(List<Transaction> transactions, Currency currency);
    List<Transaction> filterByType(Friendship f, String currentUserId, FilterType type);

    void addFriendshipExpense(String payerId, String friendId, BigDecimal payerSplit, BigDecimal friendSplit, Currency currency);

    String getCurrentPaymentStatus(User user, User friend);
    void friendAllInGroup(String groupId);

}
