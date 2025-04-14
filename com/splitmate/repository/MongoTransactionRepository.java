package com.splitmate.repository;

import com.splitmate.model.Transaction;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

public class MongoTransactionRepository implements Repository<Transaction> {
    private MongoCollection<Document> transactionCollection;
    
    public MongoTransactionRepository() {
        MongoDatabase db = MongoDBConnectionManager.getDatabase();
        transactionCollection = db.getCollection("transactions");
    }
    
    private Document transactionToDocument(Transaction transaction) {
        Document doc = new Document("amount", transaction.getAmount())
                .append("date", transaction.getDate())
                .append("senderId", transaction.getSender().getId())
                .append("receiverId", transaction.getReceiver().getId());
        if (transaction.getId() != null) {
            doc.append("_id", transaction.getId());
        }
        return doc;
    }
    
    private Transaction documentToTransaction(Document doc) {
        // For simplicity, sender and receiver details are not fully reconstructed.
        Transaction transaction = new Transaction(
            doc.getDouble("amount"),
            doc.getDate("date"),
            null,
            null
        );
        transaction.setId(doc.get("_id").toString());
        return transaction;
    }
    
    @Override
    public Transaction findById(String id) {
        Document doc = transactionCollection.find(eq("_id", id)).first();
        return doc != null ? documentToTransaction(doc) : null;
    }
    
    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        for (Document doc : transactionCollection.find()) {
            transactions.add(documentToTransaction(doc));
        }
        return transactions;
    }
    
    @Override
    public boolean insert(Transaction transaction) {
        Document doc = transactionToDocument(transaction);
        transactionCollection.insertOne(doc);
        transaction.setId(doc.getObjectId("_id").toHexString());
        return true;
    }
    
    @Override
    public boolean update(Transaction transaction) {
        Document doc = transactionToDocument(transaction);
        return transactionCollection.replaceOne(eq("_id", transaction.getId()), doc).getModifiedCount() == 1;
    }
    
    @Override
    public boolean delete(Transaction transaction) {
        return transactionCollection.deleteOne(eq("_id", transaction.getId())).getDeletedCount() == 1;
    }
}
