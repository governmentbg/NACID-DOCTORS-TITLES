package com.ext.nacid.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.web.WebKeys;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;

public class ExtAttachmentAjaxHandler extends NacidExtBaseRequestHandler {

	public ExtAttachmentAjaxHandler(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void processRequest(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(WebKeys.NEXT_SCREEN, "loading");
	}
}
