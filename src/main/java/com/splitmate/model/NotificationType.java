// File: NotificationType.java
package com.splitmate.model;

import ch.qos.logback.core.spi.BasicSequenceNumberGenerator;

public enum NotificationType {
    FRIEND_REQUEST,
    TRANSACTION_RECEIVED,
    TRANSACTION_REJECTED,
    DEBT_REMINDER,
    BUDGET_ALARM,
    RECURRING_EXPENSE
}
