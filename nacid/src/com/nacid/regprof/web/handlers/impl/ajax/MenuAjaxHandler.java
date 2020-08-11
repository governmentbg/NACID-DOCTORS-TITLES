package com.nacid.regprof.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.ext.nacid.web.handlers.MenuUtils;
import com.nacid.bl.users.User;

public class MenuAjaxHandler extends RegProfBaseRequestHandler {

    public MenuAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        User user = getLoggedUser(request, response);
        if(user == null)
            return;
        MenuUtils.processMenuClick(request, user); 
    }
}
