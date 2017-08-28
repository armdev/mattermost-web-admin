package com.project.mattermost.auth;

import com.project.mattermost.dao.UserDAO;
import com.project.mattermost.models.User;
import java.io.Serializable;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Named
//@Model
@ViewScoped
@Setter
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserLogin implements Serializable {

    private static final long serialVersionUID = 6747076553894990200L;   
    
    @Inject    
    private UserDAO userDAO;
    
   
    @Inject
    private UserContext userContext;

    private String username;

    private String privateToken;

    private String gitlabUrl;

    public UserLogin() {
    }

    public String loginUser() {
        Optional<User> user = userDAO.login(username, privateToken);
        if (user.isPresent() && user.get().getUsername() != null) {
            userContext.setUser(user.get());
            return "dashboard";
        }
        FacesMessage msg = new FacesMessage(getBundle().getString("nouser"), getBundle().getString("nouser"));
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return null;
    }

    public String getSomeString() {
        return  getBundle().getString("generaltitle");
    }

     public PropertyResourceBundle getBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{i18n}", PropertyResourceBundle.class);
    }

    public String register() {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getUsername() != null) {
            FacesMessage msg = new FacesMessage( getBundle().getString("usernameused"),  getBundle().getString("usernameused"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        } else {
            User newUser = new User();
            newUser.setPrivateToken(privateToken);
            newUser.setUsername(username);
            newUser.setUrl(gitlabUrl);
            newUser = userDAO.save(newUser);
            userContext.setUser(newUser);
            return "dashboard";
        }

    }

}
