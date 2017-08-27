package com.project.mattermost.dao;

import com.project.mattermost.connnection.*;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.project.mattermost.models.MattermostMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class IncomingDAO implements Serializable {

    private static final long serialVersionUID = 8365126215315864419L;

    @Inject
    private MongoConnection connection;

    public IncomingDAO() {

    }

    @PostConstruct
    public void init() {

    }

    public MattermostMessage saveMessage(MattermostMessage data) {
        UUID randomId = UUID.randomUUID();
        data.setId(randomId.toString());
        data.setRegisteredDate(new Date(System.currentTimeMillis()));
        final String json = connection.getGson().toJson(data);
        final BasicDBObject document = (BasicDBObject) JSON.parse(json);
        connection.getIncomingCollection().insertOne(new Document(document));
        return data;
    }

    public MattermostMessage findMessageById(String id) {
        MattermostMessage entity = null;
        Document query = new Document();
        query.put("id", id);
        for (Document doc : connection.getIncomingCollection().find(query)) {
            entity = connection.getGson().fromJson(doc.toJson(), MattermostMessage.class);
        }
        return entity;
    }

    public List<MattermostMessage> findAll() {
        final List<MattermostMessage> userList = new ArrayList<>();
        @SuppressWarnings("UnusedAssignment")
        MattermostMessage entity = new MattermostMessage();
        String sort = "registeredDate";
        String order = "desc";
        Bson sortCriteria = new BasicDBObject(sort, "desc".equals(order) ? -1 : 1);
        Document query = new Document();
        for (Document doc : connection.getUserCollection().find(query).sort(sortCriteria)) {
            entity = connection.getGson().fromJson(doc.toJson(), MattermostMessage.class);
            userList.add(entity);
        }
        return userList;
    }

    public Long getMessagesCount() {
        Long listCount = 0L;
        try {
            listCount = connection.getIncomingCollection().count();
        } catch (Exception e) {
        }
        return listCount;
    }

}
