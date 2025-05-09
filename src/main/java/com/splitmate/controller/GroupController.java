// GroupController.java
package com.splitmate.controller;

import com.splitmate.model.*;

import java.util.List;

import org.springframework.stereotype.Component;
import com.splitmate.service.*;

/**
 * Handles group-related UI actions.
 */
@Component
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    public Group createGroup(Group dto) {
        return groupService.createGroup(dto);
    }

    public Group findGroupById(String id) {
        return groupService.getGroup(id);
    }

    public Group addMember(String groupId, String userId) {
        return groupService.addUserToGroup(groupId, userId);
    }

    public Group removeMember(String groupId, String userId) {
        return groupService.removeUserFromGroup(groupId, userId);
    }

    public Group frozeMember(String groupId, String userId) {
        return groupService.frozeUserInAGroup(groupId, userId);
    }

    public Group unfreezeMember(String groupId, String userId){
        return groupService.unfreezeUserInGroup(groupId, userId);
    }

    public List<Group> getMutualGroups(String userAId, String userBId){
        return groupService.getMutualGroups(userAId, userBId);
    }
}