package com.nacid.web.filters;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.WebKeys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by georgi.georgiev on 13.01.2015.
 */
public class PermissionNosecFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        NacidDataProvider nacidDataProvider = (NacidDataProvider) getServletContext().getAttribute("nacidDataProvider");
        UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) request.getSession().getAttribute(WebKeys.LOGGED_USER);
        if (user == null) {
            user = usersDataProvider.getUserByName("test");
            user = usersDataProvider.loginUserByPass(user.getUserName(), user.getUserPass(), true, (Integer) getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID));
            if (user == null) {
                SecurityContextHolder.getContext().setAuthentication(null);
                RequestDispatcher reqDisp = request.getRequestDispatcher("/accessdenied.jsp");
                reqDisp.forward(request, servletResponse);
                return;
            } else {
                request.getSession().setAttribute(WebKeys.LOGGED_USER, user);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
