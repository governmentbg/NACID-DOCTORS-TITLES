
package com.nacid.web.filters;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UserSysLogOperation;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("applicationChangesLogger")
public class ApplicationChangesLogger extends GenericFilterBean {

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
    public void destroy() {


    }

}
