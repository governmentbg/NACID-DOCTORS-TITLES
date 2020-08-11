package com.ext.nacid.web.handlers.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.nacid.bl.external.users.ExtUsersDataProvider;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.CookieUtils;
import com.nacid.web.WebKeys;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.nacid.web.model.common.SystemMessageWebModel;

public class LogoutHandler extends NacidExtBaseRequestHandler {

  public LogoutHandler(ServletContext servletContext) {
    super(servletContext);
  }
/****
 * TODO:TUK TRQBVA DA SE MISLI ZA ZAPISVANE NA danni za logoffvaneto/logonvaneto v userlog tablicata!!!
 */
  public void processRequest(HttpServletRequest request, HttpServletResponse response) {
    UsersDataProvider usersDataProvider = getNacidDataProvider().getUsersDataProvider();
    usersDataProvider.stopUserSysLogging(request.getSession().getId());
    request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Успешно излязохте от системата!"));
    request.getSession().removeAttribute(WebKeys.LOGGED_USER);
    CookieUtils.removeCookie(response, request.getSession().getServletContext().getInitParameter("pathPrefix"), WebKeys.COOKIE_USER);
    CookieUtils.removeCookie(response, request.getSession().getServletContext().getInitParameter("pathPrefix"), WebKeys.COOKIE_PASS);
    request.getSession().invalidate();
    request.setAttribute(WebKeys.NEXT_SCREEN, "login");
  }

}
