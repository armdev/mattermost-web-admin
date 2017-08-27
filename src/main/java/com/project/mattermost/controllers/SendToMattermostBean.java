package com.project.mattermost.controllers;

import com.project.mattermost.models.MattermostMessage;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;

@Named
@SessionScoped
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SendToMattermostBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String USER_AGENT = "Mozilla/5.0";

    private transient FacesContext context = null;
    private transient ExternalContext externalContext = null;
    private String sendMe;

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        externalContext = context.getExternalContext();

    }

    public void sendBackToMattermost(MattermostMessage incomingMattermost) {
        CloseableHttpClient CLIENT = HttpClients.createDefault();
        try {
            HttpPost request = new HttpPost("http://192.168.99.100:8065/hooks/98o9ozifjp8ntkagwo69eu7cfa"); //web hook incoming mattermost

            JSONObject json = new JSONObject();
            JSONObject jsonPayload = new JSONObject();

            json.put("text", incomingMattermost.getIncomingText());
            json.put("channel", incomingMattermost.getChannel_name());
            json.put("username", incomingMattermost.getUser_name());

            String sent = "payload=" + json.toString();

            StringEntity params = new StringEntity(sent, "UTF-8");
            request.addHeader("content-type", "application/x-www-form-urlencoded");

            request.setEntity(params);
            System.out.println("request " + request.toString());
            HttpResponse response = (HttpResponse) CLIENT.execute(request);
            System.out.println("Status code###### " + response.getStatusLine().getStatusCode());
            System.out.println("getReasonPhrase# " + response.getStatusLine().getReasonPhrase());
            HttpEntity entity = response.getEntity();

        } catch (IOException | ParseException ex) {
            try {
                CLIENT.close();
            } catch (IOException ex1) {
            }
        } finally {
            try {
                CLIENT.close();
            } catch (IOException ex1) {
            }
        }
    }

   

    public String getSendMe() {
        return sendMe;
    }

    public void setSendMe(String sendMe) {
        this.sendMe = sendMe;
    }

}
