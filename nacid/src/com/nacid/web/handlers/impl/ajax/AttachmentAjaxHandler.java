package com.nacid.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;

public class AttachmentAjaxHandler extends NacidBaseRequestHandler {

	public AttachmentAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(WebKeys.NEXT_SCREEN, "loading");
	}
}
