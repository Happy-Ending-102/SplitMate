// File: GroupServiceImpl.java
package com.splitmate.service;
import com.splitmate.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.splitmate.repository.GroupRepository;
import com.splitmate.repository.UserRepository;
import com.splitmate.model.Group;
import com.splitmate.model.User;


@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepo;
    private final UserRepository userRepo;

    public GroupServiceImpl(GroupRepository groupRepo, UserRepository userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Group getGroup(String id) {
        return this.groupRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Group not found with id: " + id));
    }

    @Override
    public Group createGroup(Group g) {
        return this.groupRepo.save(g);
        friendAllInGroup(g.getId());
    }

    @Override
    public Group updateGroup(Group g) {
        return this.groupRepo.save(g);
    }

    @Override
    public Group addUserToGroup(String groupId, String userId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new NoSuchElementException("Group not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));

        group.getMembers().add(user);
        user.joinGroup(group);

        groupRepo.save(group);
        userRepo.save(user);

        friendAllInGroup(groupId);

        return group;
    }

    @Override
    public Group removeUserFromGroup(String groupId, String userId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new NoSuchElementException("Group not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));

        group.getMembers().remove(user);
        user.getGroups().remove(group);
        groupRepo.save(group);
        userRepo.save(user);

        return group;
    }

    @Override
    public Group frozeUserInAGroup(String groupId, String userId) {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new NoSuchElementException("Group not found"));
        User user = userRepo.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));

        // Only freeze if the user is currently active
        if (group.getMembers().contains(user)) {
            group.getMembers().remove(user);
            group.getFrozenMembers().add(user);
        }

        return groupRepo.save(group);
    }

    @Override
    public Group unfreezeUserInGroup(String groupId, String userId) {
        Group group = groupRepo.findById(groupId)
            .orElseThrow(() -> new NoSuchElementException("Group not found"));
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (group.getFrozenMembers().contains(user)) {
            group.getFrozenMembers().remove(user);
            group.getMembers().add(user);
        }

        return groupRepo.save(group);
    }

    @Override
    public String getAvatarBase64(String groupId) {
    return groupRepo.findById(groupId)
                    .map(Group::getAvatarBase64)
                    .orElse("");
    }

    @Override
    public Group updateAvatar(String groupId, String base64) {
    Group g = getGroup(groupId);
    g.setAvatarBase64(base64);
    return groupRepo.save(g);
    }

    @Override
    public List<Group> getMutualGroups(String userAId, String userBId) {
        // 1) Load both users (or fail early)
    User userA = userRepo.findById(userAId)
        .orElseThrow(() -> new NoSuchElementException("User not found: " + userAId));
    User userB = userRepo.findById(userBId)
        .orElseThrow(() -> new NoSuchElementException("User not found: " + userBId));

    // 2) Find all groups that userA belongs to
    List<Group> groupsOfA = groupRepo.findByMembersContaining(userA);

    // 3) Filter only those groups where userB is also a member
    return groupsOfA.stream()
        .filter(g -> g.getMembers().contains(userB))
        .collect(Collectors.toList());
    }

    @Override
    public List<User> getPossiblMembersToAdd(String groupId, String userId) {
        Group group = groupRepo.findById(groupId)
            .orElseThrow(() -> new NoSuchElementException("Group not found"));
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Get all frinds of the user who are not already members of the group
        return user.getFriends().stream()
            .filter(friend -> !group.getMembers().contains(friend))
            .filter(friend -> !group.getFrozenMembers().contains(friend))
            .collect(Collectors.toList());
    }

    @Override
    public void friendAllInGroup(String groupId) {
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
}
