package com.splitmate.controller;

import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.splitmate.repository.MongoGroupRepository;

public class GroupController {
    private MongoGroupRepository groupRepo;
    
    public GroupController() {
        groupRepo = new MongoGroupRepository();
    }
    
    public boolean createGroup(String groupName, String defaultCurrency) {
        Group group = new Group(groupName, defaultCurrency);
        return groupRepo.insert(group);
    }
    
    public boolean addMember(String groupId, User user) {
        Group group = groupRepo.findById(groupId);
        if (group != null) {
            group.addMember(user);
            return groupRepo.update(group);
        }
        return false;
    }
    
    public boolean removeMember(String groupId, User user) {
        Group group = groupRepo.findById(groupId);
        if (group != null) {
            group.removeMember(user);
            return groupRepo.update(group);
        }
        return false;
    }
}
