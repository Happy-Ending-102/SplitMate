package com.splitmate.model;

import java.util.Date;

/**
 * A concrete notification for payment reminders.
 */
public class PaymentReminderNotification extends AbstractNotification {
    public PaymentReminderNotification(String message, Date date) {
        super(message, date);
    }
    
    @Override
    public void sendNotification() {
        // Implement your notification logic; here we simply print out the message.
        System.out.println("Payment Reminder: " + message);
    }
}
