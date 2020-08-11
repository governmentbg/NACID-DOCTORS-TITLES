package com.nacid.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.MenuUtils;

public class MenuAjaxHandler extends NacidBaseRequestHandler {

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
