package com.splitmate.repository;

import com.splitmate.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

public class MongoUserRepository implements Repository<User> {
    private MongoCollection<Document> userCollection;
    
    public MongoUserRepository() {
        MongoDatabase db = MongoDBConnectionManager.getDatabase();
        userCollection = db.getCollection("users");
    }
    
    private Document userToDocument(User user) {
        Document doc = new Document("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("iban", user.getIban());
        if (user.getId() != null) {
            doc.append("_id", user.getId());
        }
        return doc;
    }
    
    private User documentToUser(Document doc) {
        User user = new User(
            doc.getString("name"),
            doc.getString("email"),
            doc.getString("password"),
            doc.getString("iban")
        );
        user.setId(doc.getObjectId("_id").toHexString());
        return user;
    }
    
    @Override
    public User findById(String id) {
        Document doc = userCollection.find(eq("_id", id)).first();
        return doc != null ? documentToUser(doc) : null;
    }
    
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        for (Document doc : userCollection.find()) {
            users.add(documentToUser(doc));
        }
        return users;
    }
    
    @Override
    public boolean insert(User user) {
        Document doc = userToDocument(user);
        userCollection.insertOne(doc);
        user.setId(doc.getObjectId("_id").toHexString());
        return true;
    }
    
    @Override
    public boolean update(User user) {
        Document doc = userToDocument(user);
        return userCollection.replaceOne(eq("_id", user.getId()), doc).getModifiedCount() == 1;
    }
    
    @Override
    public boolean delete(User user) {
        return userCollection.deleteOne(eq("_id", user.getId())).getDeletedCount() == 1;
    }
}
