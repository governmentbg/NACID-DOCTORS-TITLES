package com.nacid.web.handlers.impl;

import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.CookieUtils;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.model.common.SystemMessageWebModel;

public class LoginHandler extends NacidBaseRequestHandler {

  public LoginHandler(ServletContext servletContext) {
    super(servletContext);
  }
  /****
   * TODO:TUK TRQBVA DA SE MISLI ZA ZAPISVANE NA danni za logoffvaneto/logonvaneto v userlog tablicata!!!
   */
  public void processRequest(HttpServletRequest request, HttpServletResponse response) {
    request.setAttribute(WebKeys.NEXT_SCREEN, "login");
    NacidDataProvider nacidDataProvider = getNacidDataProvider();
    UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
    HttpSession session = request.getSession();
    boolean formSubmitted = RequestParametersUtils.getParameterFormSubmitted(request);
    if (formSubmitted) {
      String username = request.getParameter("username");
      String password = request.getParameter("password");
      if (username != null && password != null) {
        User user = usersDataProvider.loginUserByPass(username, password, false, (Integer) request.getSession().getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID));
        if (user == null) {
          request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("грешно име или парола"));
        } else {
          /**
           * logofva stariq user ot bazata, i dobavq syslogrecord za noviq!!!!
           */
          usersDataProvider.stopUserSysLogging(session.getId());
          int webApplicationId = (Integer) getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID);
          usersDataProvider.startUserSysLogging(user.getUserId(), session.getId(), request.getRemoteAddr(), request.getRemoteHost(), webApplicationId);
          boolean autologin = DataConverter.parseBoolean(request.getParameter("autologin"));
          if (autologin) {
            Calendar expirationDate = Calendar.getInstance();
            expirationDate.add(Calendar.YEAR, 1);
            CookieUtils.setCookie(response, WebKeys.COOKIE_USER, username, session.getServletContext().getInitParameter("pathPrefix"), true, expirationDate.getTime());
            CookieUtils.setCookie(response, WebKeys.COOKIE_PASS, user.getUserPass(), session.getServletContext().getInitParameter("pathPrefix"), true, expirationDate.getTime());
          }
          session.setAttribute(WebKeys.LOGGED_USER, user);
          //Generirane na menuto na toku shto lognatiq user
          MenuShowHandler mrh = new MenuShowHandler(request.getSession().getServletContext());
          mrh.processRequest(request, response);
          new HomePageHandler(session.getServletContext()).processRequest(request, response);
          
          
        }
        
      }
    }
    
  }

}
