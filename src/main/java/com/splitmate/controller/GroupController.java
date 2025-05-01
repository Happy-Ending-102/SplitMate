// GroupController.java
package com.splitmate.controller;

import com.splitmate.model.*;
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
        throw new UnsupportedOperationException();
    }

    public Group findGroupById(String id) {
        throw new UnsupportedOperationException();
    }

    public Group addMember(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }

    public Group removeMember(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }

    public Group frozeMember(String groupId, String userId) {
        throw new UnsupportedOperationException();
    }
}