package com.nacid.regprof.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UserSysLogOperation;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;

public class ApplicationChangesLogger implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        chain.doFilter(request, response);
        NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(request.getSession());
        UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
        Integer applicationId = (Integer) request.getAttribute(WebKeys.APPLICATION_ID);
        User user = NacidBaseRequestHandler.getLoggedNacidUser(request, response);
        UserSysLogOperation operation = (UserSysLogOperation) request.getAttribute("operation");
        if (applicationId != null) {
            usersDataProvider.addApplicationChangeHistoryRecord(applicationId, user.getUserId(), operation.getDateCreated(), operation.getOperationName(), operation.getGroupName());
        }
        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void destroy() {


    }

}
