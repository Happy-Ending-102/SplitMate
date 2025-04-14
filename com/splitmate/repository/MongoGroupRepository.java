package com.splitmate.repository;

import com.splitmate.model.Group;
import com.splitmate.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

public class MongoGroupRepository implements Repository<Group> {
    private MongoCollection<Document> groupCollection;
    
    public MongoGroupRepository() {
        MongoDatabase db = MongoDBConnectionManager.getDatabase();
        groupCollection = db.getCollection("groups");
    }
    
    private Document groupToDocument(Group group) {
        Document doc = new Document("groupName", group.getGroupName())
                .append("defaultCurrency", group.getDefaultCurrency());
        // Convert members to a list of user IDs:
        List<String> memberIds = new ArrayList<>();
        for (User u : group.getMembers()) {
            if (u.getId() != null) {
                memberIds.add(u.getId());
            }
        }
        doc.append("memberIds", memberIds);
        if (group.getId() != null) {
            doc.append("_id", group.getId());
        }
        return doc;
    }
    
    private Group documentToGroup(Document doc) {
        Group group = new Group(doc.getString("groupName"), doc.getString("defaultCurrency"));
        group.setId(doc.get("_id").toString());
        return group;
    }
    
    @Override
    public Group findById(String id) {
        Document doc = groupCollection.find(eq("_id", id)).first();
        return doc != null ? documentToGroup(doc) : null;
    }
    
    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        for (Document doc : groupCollection.find()) {
            groups.add(documentToGroup(doc));
        }
        return groups;
    }
    
    @Override
    public boolean insert(Group group) {
        Document doc = groupToDocument(group);
        groupCollection.insertOne(doc);
        group.setId(doc.getObjectId("_id").toHexString());
        return true;
    }
    
    @Override
    public boolean update(Group group) {
        Document doc = groupToDocument(group);
        return groupCollection.replaceOne(eq("_id", group.getId()), doc).getModifiedCount() == 1;
    }
    
    @Override
    public boolean delete(Group group) {
        return groupCollection.deleteOne(eq("_id", group.getId())).getDeletedCount() == 1;
    }
}
