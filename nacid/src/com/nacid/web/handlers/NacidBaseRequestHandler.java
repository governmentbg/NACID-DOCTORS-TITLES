package com.nacid.web.handlers;

import com.nacid.bl.NacidDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.impl.MenuShowHandler;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class NacidBaseRequestHandler extends BaseRequestHandler {
  
  public NacidBaseRequestHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
	  super(nacidDataProvider, groupId, servletContext);
	  
	  
  }
  public NacidBaseRequestHandler(ServletContext servletContext) {
    super(servletContext);
      
  }
  
  public void processRequest(HttpServletRequest request, HttpServletResponse response) {
    try {
        super.processRequest(request, response);
    } catch (NoAccessException e) {
        SystemMessageWebModel error = new SystemMessageWebModel("Сесията ви е изтекла или нямате права за да отворите желаната страница.");
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, error);
        request.setAttribute(WebKeys.NEXT_SCREEN, "accessdenied");

    }
  }


  public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
	  request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel());
      throw new RuntimeException("Операцията за " + request.getPathInfo() + " не е дефинирана ");
  }
  
  public void initHandler(HttpServletRequest request, HttpServletResponse response) {
      if(!(Boolean)request.getAttribute("ajaxServlet")) {
          MenuShowHandler mrh = new MenuShowHandler(request.getSession().getServletContext());
          mrh.processRequest(request, response);
      }
  }
  
  
  
  
  
}
