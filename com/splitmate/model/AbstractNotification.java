package com.splitmate.model;

import java.util.Date;

/**
 * AbstractNotification forces all notifications to implement sendNotification().
 * This demonstrates polymorphism via abstract methods.
 */
public abstract class AbstractNotification extends AbstractModel {
    protected String message;
    protected Date date;
    
    public AbstractNotification(String message, Date date) {
        this.message = message;
        this.date = date;
    }
    
    // Every subclass must implement its own sendNotification logic.
    public abstract void sendNotification();
    
    // Getters
    public String getMessage() { return message; }
    public Date getDate() { return date; }
}
