// File: Expense.java
package com.splitmate.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("expenses")
public abstract class Expense extends BaseEntity {
    @DBRef protected User owner;
    protected BigDecimal amount;
    protected Currency currency;
    protected String description;
    protected String billImageB64;
    protected LocalDate date;
    @DBRef protected List<Partition> divisionAmongUsers;
    @DBRef protected Group group; // null olabilir. null ise iki kisi arasında bir harcama var demektir.

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public abstract Map<User, BigDecimal> calculateShares();

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Currency getCurrency() { return currency; }
    public void setCurrency(Currency currency) { this.currency = currency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBillImageB64() { return billImageB64; }
    public void setBillImageB64(String billImageUrl) { this.billImageB64 = billImageUrl; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<Partition> getDivisionAmongUsers() { return divisionAmongUsers; }
    public void setDivisionAmongUsers(List<Partition> divisionAmongUsers) { this.divisionAmongUsers = divisionAmongUsers; }

    
}
