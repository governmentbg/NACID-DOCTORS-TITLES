package com.nacid.regprof.web.handlers.impl.ajax;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.WebKeys;

public class RegprofAttachmentAjaxHandler extends RegProfBaseRequestHandler {
    public RegprofAttachmentAjaxHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WebKeys.NEXT_SCREEN, "loading");
    }
}
