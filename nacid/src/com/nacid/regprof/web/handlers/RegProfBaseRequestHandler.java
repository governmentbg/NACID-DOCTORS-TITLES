package com.nacid.regprof.web.handlers;

import com.nacid.bl.NacidDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.BaseRequestHandler;
import com.nacid.web.handlers.NoAccessException;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RegProfBaseRequestHandler extends BaseRequestHandler {

    public RegProfBaseRequestHandler(NacidDataProvider nacidDataProvider, Integer groupId, ServletContext servletContext) {
        super(nacidDataProvider, groupId, servletContext);
    }

    public RegProfBaseRequestHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            super.processRequest(request, response);    
        } catch (NoAccessException e) {
            SystemMessageWebModel error = new SystemMessageWebModel("Сесията ви е изтекла или нямате права за да отворите желаната страница.");
            request.setAttribute(WebKeys.NEXT_SCREEN, "accessdenied");
            return;
        }
        
    }
    public void initHandler(HttpServletRequest request, HttpServletResponse response) {
        if(!(Boolean)request.getAttribute("ajaxServlet")) {
            MenuShowHandler mrh = new MenuShowHandler(request.getSession().getServletContext());
            mrh.processRequest(request, response);
        }
    }
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("Операцията за " + request.getPathInfo() + " не е дефинирана ");
    }
}
