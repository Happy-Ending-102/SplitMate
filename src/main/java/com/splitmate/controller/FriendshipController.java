// File: FriendshipController.java
package com.splitmate.controller;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.splitmate.model.Friendship;
import com.splitmate.model.Payment;
import com.splitmate.service.FriendshipService;



@Component
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    /** Recipient calls this to accept a friend request. */
    public void acceptFriendRequest(String requesterId, String recipientId){
        friendshipService.acceptFriendRequest(requesterId, recipientId);
    }

     /** Get payments sorted by amount ASC */
    @GetMapping("/{friendshipId}/history/amount/asc")
    public List<Payment> getHistorySortedByAmountAsc(@PathVariable String friendshipId) {
        Friendship friendship = friendshipService.getFriendshipById(friendshipId);
        return friendshipService.sortByAmountAsc(friendship);
    }

    /** Get payments sorted by amount DESC */
    @GetMapping("/{friendshipId}/history/amount/desc")
    public List<Payment> getHistorySortedByAmountDesc(@PathVariable String friendshipId) {
        Friendship friendship = friendshipService.getFriendshipById(friendshipId);
        return friendshipService.sortByAmountDesc(friendship);
    }

    /** Get payments sorted by payment date DESC */
    @GetMapping("/{friendshipId}/history/date/desc")
    public List<Payment> getHistorySortedByDateDesc(@PathVariable String friendshipId) {
        Friendship friendship = friendshipService.getFriendshipById(friendshipId);
        return friendshipService.sortByDateDesc(friendship);
    }

    public Friendship getFriendshipBetween(String userAId, String userBId){
        return friendshipService.getFriendshipBetween(userAId, userBId);
    }
}