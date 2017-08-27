package com.project.mattermost.incoming;

import com.project.mattermost.controllers.SendToMattermostBean;
import com.project.mattermost.dao.IncomingDAO;
import com.project.mattermost.models.MattermostMessage;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

@WebServlet(urlPatterns = {"/mattermostWebHook"}, initParams = {
    @WebInitParam(name = "token",
            value = "q78phkmopidabj3gugdnwxgd8o")})
public class MattermostWebHook extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private IncomingDAO incomingDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("MattermmostWebhook servlet init");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("MattermmostWebhook Servlet doGet called, no action needed");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("MattermmostWebhook Servlet doPost called");

            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {

                String paramName = parameterNames.nextElement();
                System.out.println(paramName);
                System.out.println("n");

                String[] paramValues = request.getParameterValues(paramName);
                for (int i = 0; i < paramValues.length; i++) {
                    String paramValue = paramValues[i];
                    System.out.println("t " + paramValue);
                    System.out.println("n");
                }

            }

            Enumeration<String> headerNames = request.getHeaderNames();

            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                System.out.println("headerName  " + headerName);
                Enumeration<String> headers = request.getHeaders(headerName);
                while (headers.hasMoreElements()) {
                    String headerValue = headers.nextElement();
                    System.out.println("headerValue  " + headerValue);

                }
            }

            Map<String, String[]> paramMap = request.getParameterMap();

            MattermostMessage mattermostRequest = new MattermostMessage();

            String channel_id = request.getParameter("channel_id");

            String channel_name = request.getParameter("channel_name");

            String command = request.getParameter("command");

            String response_url = request.getParameter("response_url");

            String team_domain = request.getParameter("team_domain");

            String team_id = request.getParameter("team_id");

            String text = request.getParameter("text");

            String token = request.getParameter("token");

            String user_id = request.getParameter("user_id");

            String user_name = request.getParameter("user_name");

            mattermostRequest.setChannel_id(channel_id);
            mattermostRequest.setChannel_name(channel_name);
            mattermostRequest.setCommand(command);
            mattermostRequest.setResponse_url(response_url);
            mattermostRequest.setTeam_domain(team_domain);

            mattermostRequest.setTeam_id(team_id);
            mattermostRequest.setIncomingText(text);

            mattermostRequest.setUser_id(user_id);
            mattermostRequest.setUser_name(user_name);

            mattermostRequest.setIncomingText("This is response number " + System.currentTimeMillis());

            incomingDAO.saveMessage(mattermostRequest);
            SendToMattermostBean mattermostBean = new SendToMattermostBean();
            mattermostBean.sendBackToMattermost(mattermostRequest);
        } catch (Exception ex) {
        }
    }

    @Override
    public void destroy() {

    }

}
