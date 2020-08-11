package com.nacid.web.filters;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UserSysLogOperation;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class UserOperationsLogger implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		request.setCharacterEncoding("UTF-8");
		HttpServletResponse response = (HttpServletResponse) resp;
		//ne logva login operaciite, zashtoto tam sa useri i paroli!
		if (!"login".equals(NacidBaseRequestHandler.getGroupName(request))) {
			HttpSession session = request.getSession();
			User user = NacidBaseRequestHandler.getLoggedNacidUser(request, response);
			NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(request.getSession());
			UsersDataProvider usersDataProvider = nacidDataProvider.getUsersDataProvider();
			String groupName = NacidBaseRequestHandler.getGroupName(request);
			String operationName = NacidBaseRequestHandler.getOperationName(request);
			String queryString = getQueryString(request);
			UserSysLogOperation operation = usersDataProvider.addUserSysLogOperation(user.getUserId(), session.getId(), request.getRemoteAddr(), request.getRemoteHost(), groupName, operationName, queryString);
			request.setAttribute("operation", operation);
		}
		filterChain.doFilter(req, resp);
	}
	@Override
	public void destroy() {


	}
	private String getQueryString(HttpServletRequest request) {
		Enumeration<?> paramNames = request.getParameterNames();
		List<String> parameters = new ArrayList<String>();
		while (paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();
			String paramValue = request.getParameter(paramName);
			parameters.add(paramName + "=" + paramValue);
		}
		if (parameters.size() > 0) {
			return StringUtils.join(parameters, "&");
		}
		return "";
	}



}
