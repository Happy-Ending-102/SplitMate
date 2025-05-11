package com.splitmate.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("Debts")
public class Debt extends Payment{
    static final Currency CURRENCY = Currency.TRY;
}
