package com.ext.nacid.regprof.web.handlers.impl.applications;

import com.ext.nacid.web.handlers.impl.applications.SuccessSignHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Georgi
 * Date: 17.8.2020 г.
 * Time: 23:35
 */
public class ExtRegprofSuccessSignHandler extends SuccessSignHandler {
    public ExtRegprofSuccessSignHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        processSuccessSign(request, response, "applications");
    }
}
