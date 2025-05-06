// File: GroupService.java
package com.splitmate.service;

import com.splitmate.model.Group;

public interface GroupService {
    Group getGroup(String id);
    Group createGroup(Group g);
    Group addUserToGroup(String groupId, String userId);
    Group removeUserFromGroup(String groupId, String userId);
    Group frozeUserInAGroup(String groupId, String userId);
    Group unfreezeUserInGroup(String groupId, String userId);
    String getAvatarBase64(String groupId);
    Group updateAvatar(String groupId, String base64);
}
