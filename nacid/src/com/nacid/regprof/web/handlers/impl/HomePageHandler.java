package com.nacid.regprof.web.handlers.impl;

import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.WebKeys;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomePageHandler extends RegProfBaseRequestHandler {

  public HomePageHandler(ServletContext servletContext) {
    super(servletContext);
  }

  public void processRequest(HttpServletRequest request, HttpServletResponse response) {
    request.setAttribute(WebKeys.NEXT_SCREEN, "home");
  }

}
