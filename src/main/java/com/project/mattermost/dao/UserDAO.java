package com.project.mattermost.dao;

import com.project.mattermost.connnection.*;
import com.project.mattermost.models.User;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.bson.Document;
import org.bson.conversions.Bson;

@Named
@SessionScoped
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDAO implements Serializable {

    private static final long serialVersionUID = 8365126215315864419L;

    @Inject
    private MongoConnection connection;

    public UserDAO() {

    }

    @PostConstruct
    public void init() {

    }

    public User save(User data) {
        UUID userId = UUID.randomUUID();
        data.setId(userId.toString());
        final String json = connection.getGson().toJson(data);
        final BasicDBObject document = (BasicDBObject) JSON.parse(json);
        connection.getUserCollection().insertOne(new Document(document));
        return data;
    }

    public User findById(String id) {
        User entity = null;
        Document query = new Document();
        query.put("id", id);
        for (Document doc : connection.getUserCollection().find(query)) {
            entity = connection.getGson().fromJson(doc.toJson(), User.class);
        }
        return entity;
    }

    public User findByUsername(String username) {
        User entity = null;
        Document query = new Document();
        query.put("username", username);
        for (Document doc : connection.getUserCollection().find(query)) {
            entity = connection.getGson().fromJson(doc.toJson(), User.class);
        }
        return entity;
    }

    public List<User> findAll() {
        final List<User> userList = new ArrayList<>();
        @SuppressWarnings("UnusedAssignment")
        User entity = new User();
        String sort = "registeredDate";
        String order = "desc";
        Bson sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        Document query = new Document();
        for (Document doc : connection.getUserCollection().find(query).sort(sortCriteria)) {
            entity = connection.getGson().fromJson(doc.toJson(), User.class);
            userList.add(entity);
        }
        return userList;
    }

    public boolean updateProfile(String userId, User entity) {
        try {

            Bson id = new Document("id", userId);

            Bson username = new Document("username", entity.getUsername());
            Bson url = new Document("url", entity.getUrl());
            Bson privateToken = new Document("privateToken", entity.getPrivateToken());

            Bson usernameUp = new Document("$set", username);
            Bson privateTokenUp = new Document("$set", privateToken);
            Bson urlUp = new Document("$set", url);

            connection.getUserCollection().updateOne(
                    id,
                    usernameUp
            );
            connection.getUserCollection().updateOne(
                    id,
                    urlUp
            );
            connection.getUserCollection().updateOne(
                    id,
                    privateTokenUp
            );

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUsersCount() {
        Long listCount = 0L;
        try {
            listCount = connection.getUserCollection().count();
        } catch (Exception e) {
        }
        return listCount;
    }

    public Optional<User> login(String username, String privateToken) {
        User entity = null;
        Document query = new Document();
        query.put("username", username.trim());
        query.put("privateToken", privateToken.trim());
        for (Document doc : connection.getUserCollection().find(query)) {
            entity = connection.getGson().fromJson(doc.toJson(), User.class);
        }
        return Optional.ofNullable(entity);
    }

}
