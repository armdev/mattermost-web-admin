package com.project.mattermost.models;

import java.io.Serializable;
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
public class User implements Serializable {

    private static final long serialVersionUID = -3192576805761029194L;

    private ObjectId _id;

    private String id;

    private String username;

    private String url;

    private String privateToken;

    private Date registeredDate;

}
