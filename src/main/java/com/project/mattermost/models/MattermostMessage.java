package com.project.mattermost.models;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class MattermostMessage {

    private ObjectId _id;

    private String id;
    private String channel_id;
    private String channel_name;
    private String command;
    private String response_url;
    private String team_domain;
    private String team_id;
    private String incomingText;
    private String token;
    private String user_id;
    private String user_name;

    private Date registeredDate;

}
