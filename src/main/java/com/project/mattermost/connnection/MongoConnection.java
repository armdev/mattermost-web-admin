package com.project.mattermost.connnection;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.Serializable;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.Document;

@Named
@ApplicationScoped
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MongoConnection implements Serializable {

    private static final long serialVersionUID = 8365126215315864419L;

    private final String mongohost = "localhost";
    private final int mongoport = 27017;
    private final String mongoDB = "micro-monolit-db";
    private MongoCollection<Document> userCollection;
    private MongoCollection<Document> incomingCollection;
    private final Gson gson = new Gson();

    public MongoConnection() {

    }

    @PostConstruct
    public void init() {
        MongoClientOptions options = MongoClientOptions.builder().connectionsPerHost(100).minConnectionsPerHost(0).threadsAllowedToBlockForConnectionMultiplier(5)
                .connectTimeout(30000).maxWaitTime(120000).maxConnectionIdleTime(0).maxConnectionLifeTime(0).connectTimeout(10000).socketTimeout(0)
                .socketKeepAlive(false).heartbeatFrequency(10000).minHeartbeatFrequency(500).heartbeatConnectTimeout(20000).localThreshold(15)
                .build();

        MongoClient mongo = new MongoClient(Arrays.asList(
                new ServerAddress(mongohost, mongoport)),
                options);

        MongoDatabase db = mongo.getDatabase(mongoDB);
        userCollection = db.getCollection("user");
        incomingCollection = db.getCollection("incoming_messages");
        
        
    }

}
