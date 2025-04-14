package com.splitmate.model;

// Common base class for all domain entities.
public abstract class AbstractModel {
    protected String id; // Using String to match MongoDB ObjectId as a hex string.
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}
