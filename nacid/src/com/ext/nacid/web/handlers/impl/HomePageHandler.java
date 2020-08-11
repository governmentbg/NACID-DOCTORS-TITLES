package com.ext.nacid.web.handlers.impl;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

//import com.nacid.bl.external.users.ExtUser;

public class HomePageHandler extends NacidExtBaseRequestHandler {
    private String loginUrl;
    public HomePageHandler(ServletContext servletContext) {
        super(servletContext);
        try {
            Properties p = new Properties();
            p.load(getClass().getClassLoader().getResourceAsStream("spring-security.properties"));
            loginUrl = p.getProperty("ext.application.login.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        User user = getLoggedUser(request, response);
        if (user.getUserId() == User.ANONYMOUS_USER_ID) {
            request.setAttribute(WebKeys.NEXT_SCREEN, "login");
            request.setAttribute("loginUrl", loginUrl);
        } else {
            request.setAttribute(WebKeys.NEXT_SCREEN, "home");
        }
    }
}
