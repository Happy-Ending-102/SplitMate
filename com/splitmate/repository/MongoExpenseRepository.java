package com.splitmate.repository;

import com.splitmate.model.Expense;
import com.splitmate.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoExpenseRepository implements Repository<Expense> {
    private MongoCollection<Document> expenseCollection;
    
    public MongoExpenseRepository() {
        MongoDatabase db = MongoDBConnectionManager.getDatabase();
        expenseCollection = db.getCollection("expenses");
    }
    
    private Document expenseToDocument(Expense expense) {
        Document doc = new Document("amount", expense.getAmount())
                .append("description", expense.getDescription())
                .append("currency", expense.getCurrency())
                .append("date", expense.getDate())
                .append("payerId", expense.getPayer().getId()); // store payer's id
        // (For simplicity, splitDetails are omitted; extend as needed.)
        if (expense.getId() != null) {
            doc.append("_id", expense.getId());
        }
        return doc;
    }
    
    private Expense documentToExpense(Document doc) {
        // For a complete solution, retrieve the payer via UserRepository.
        User dummyPayer = new User("dummy", "dummy@example.com", "dummy", "dummyIBAN");
        dummyPayer.setId(doc.getString("payerId"));
        Expense expense = new Expense(
            doc.getDouble("amount"),
            doc.getString("description"),
            doc.getString("currency"),
            doc.getDate("date"),
            dummyPayer,
            null // skip splitDetails conversion
        );
        expense.setId(doc.get("_id").toString());
        return expense;
    }
    
    @Override
    public Expense findById(String id) {
        Document doc = expenseCollection.find(eq("_id", id)).first();
        return doc != null ? documentToExpense(doc) : null;
    }
    
    @Override
    public List<Expense> findAll() {
        List<Expense> expenses = new ArrayList<>();
        for (Document doc : expenseCollection.find()) {
            expenses.add(documentToExpense(doc));
        }
        return expenses;
    }
    
    @Override
    public boolean insert(Expense expense) {
        Document doc = expenseToDocument(expense);
        expenseCollection.insertOne(doc);
        expense.setId(doc.getObjectId("_id").toHexString());
        return true;
    }
    
    @Override
    public boolean update(Expense expense) {
        Document doc = expenseToDocument(expense);
        return expenseCollection.replaceOne(eq("_id", expense.getId()), doc).getModifiedCount() == 1;
    }
    
    @Override
    public boolean delete(Expense expense) {
        return expenseCollection.deleteOne(eq("_id", expense.getId())).getDeletedCount() == 1;
    }
}
