package com.ext.nacid.web.handlers;

import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.BaseRequestHandler;
import com.nacid.web.handlers.NoAccessException;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.nacid.bl.external.users.ExtUser;
//import com.nacid.bl.external.users.ExtUsersDataProvider;

public abstract class NacidExtBaseRequestHandler extends BaseRequestHandler {

    public NacidExtBaseRequestHandler(ServletContext servletContext) {
        super(servletContext);
    }


    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        User user = getLoggedUser(request, response);

        try { 
            super.processRequest(request, response);
        } catch(NoAccessException e) { // User has no access to view the page!
            SystemMessageWebModel error = new SystemMessageWebModel("Сесията ви е изтекла или нямате права за да отворите желаната страница.");
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, error);
            request.setAttribute(WebKeys.NEXT_SCREEN, "accessdenied");
        }

    }
    /*public static final User getNacidLoggedExtUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(WebKeys.LOGGED_USER);
        if (user != null) {
            return user;
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider(request.getSession());
        UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();

        String userName = CookieUtils.getCookieValue(request, WebKeys.COOKIE_USER);
        String userPass = CookieUtils.getCookieValue(request, WebKeys.COOKIE_PASS);
        user = usersDataProvider.loginUserByPass(userName, userPass, true, (Integer) request.getSession().getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID));
        if (user == null) {
            String addr = request.getRemoteAddr();
            user = usersDataProvider.loginUserByIp(addr);
        }
        if (user == null) {
            user = usersDataProvider.loginAnonymousUser();
        }
        session.setAttribute(WebKeys.LOGGED_USER, user);
        return user;

    }
    final public User getLoggedUser(HttpServletRequest request, HttpServletResponse response) {
        return getNacidLoggedExtUser(request, response);
    }*/

   
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        //request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Операцията за " + request.getPathInfo() + " не е дефинирана "));
        throw new RuntimeException("Операцията за " + request.getPathInfo() + " не е дефинирана ");
    }
    public ExtPerson getExtPerson(HttpServletRequest request, HttpServletResponse response) {
        ExtPerson extPerson = (ExtPerson) request.getSession().getAttribute("extPerson");
        if (extPerson == null) {
            User user = getLoggedUser(request, response);
            extPerson = getNacidDataProvider().getExtPersonDataProvider().getExtPersonByUserId(user.getUserId());
            request.getSession().setAttribute("extPerson", extPerson);
        }
        return extPerson;
    }

    public void initHandler(HttpServletRequest request, HttpServletResponse response) {
        if(!(Boolean)request.getAttribute("ajaxServlet")) {
            MenuShowHandler mrh = new MenuShowHandler(request.getSession().getServletContext());
            mrh.processRequest(request, response);
        }
    }
}