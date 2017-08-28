package com.project.mattermost.auth;


import com.project.mattermost.models.User;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Named
@SessionScoped
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserContext implements Serializable {

    private static final long serialVersionUID = 5497292482670000840L;
    
    @Setter
    @Getter
    private User user = new User();
  
 

}
