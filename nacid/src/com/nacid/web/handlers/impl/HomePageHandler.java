package com.nacid.web.handlers.impl;

import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomePageHandler extends NacidBaseRequestHandler {

  public HomePageHandler(ServletContext servletContext) {
    super(servletContext);
  }

  public void processRequest(HttpServletRequest request, HttpServletResponse response) {
    //writeToResponse(response, "inside homepagehandler....");
    User user = getLoggedUser(request, response);
    /*if (user.getUserId() == User.ANONYMOUS_USER_ID) {
      new LoginHandler(request.getSession().getServletContext()).processRequest(request, response);
      return;
    }*/
    request.setAttribute(WebKeys.NEXT_SCREEN, "home");
  }

}
