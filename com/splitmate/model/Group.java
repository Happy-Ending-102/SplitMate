package com.splitmate.model;

import java.util.ArrayList;
import java.util.List;

public class Group extends AbstractModel {
    private String groupName;
    private String defaultCurrency;
    private List<User> members;
    
    public Group(String groupName, String defaultCurrency) {
        this.groupName = groupName;
        this.defaultCurrency = defaultCurrency;
        this.members = new ArrayList<>();
    }
    
    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }
    
    public void removeMember(User user) {
        members.remove(user);
    }
    
    // Getters
    public String getGroupName() { return groupName; }
    public String getDefaultCurrency() { return defaultCurrency; }
    public List<User> getMembers() { return members; }
}
