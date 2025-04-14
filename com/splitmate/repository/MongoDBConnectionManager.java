package com.splitmate.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectionManager {
    private static final String CONNECTION_STRING = "mongodb://your_username:your_password@your-remote-host:27017";
    private static final String DATABASE_NAME = "splitmate";
    private static MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
    private static MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
    
    public static MongoDatabase getDatabase() {
        return database;
    }
    
    // Optionally, add a method to close the client at shutdown.
}
