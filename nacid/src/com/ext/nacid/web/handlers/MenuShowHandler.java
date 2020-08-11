package com.ext.nacid.web.handlers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.MenuUtils;

public class MenuShowHandler extends NacidExtBaseRequestHandler {

	public MenuShowHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		User user = getLoggedUser(request, response);
		if(user == null)
			return;
	    request.setAttribute(WebKeys.MENU_WEB_MODEL, MenuUtils.getMenuWebModel(request, user)); 
	}
}
