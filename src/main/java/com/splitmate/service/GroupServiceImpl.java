// File: GroupServiceImpl.java
package com.splitmate.service;
import com.splitmate.model.User;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import com.splitmate.repository.GroupRepository;
import com.splitmate.repository.UserRepository;
import com.splitmate.model.Group;

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
}
