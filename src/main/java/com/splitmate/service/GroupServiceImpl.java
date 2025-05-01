// File: GroupServiceImpl.java
package com.splitmate.service;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public Group createGroup(Group g) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Group addUserToGroup(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Group removeUserFromGroup(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Group frozeUserInAGroup(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }
}
