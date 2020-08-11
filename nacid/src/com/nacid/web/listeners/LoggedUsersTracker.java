package com.nacid.web.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.User;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.NacidBaseRequestHandler;
/**
 * pri unishtojavane na sesiq za daden potrebitel (kogato user-a e izlqzal ot site-a bez da se logoffne)
 * dobavq v bazata 4asa na unishtojavaneto kato time logoff...
 * @author ggeorgiev
 */
public class LoggedUsersTracker implements HttpSessionListener {
	public void sessionCreated(HttpSessionEvent se) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		User user = (User) se.getSession().getAttribute(WebKeys.LOGGED_USER);
		if (user != null) {
			NacidDataProvider nacidDataProvider = NacidBaseRequestHandler.getNacidDataProvider(se.getSession());
			UsersDataProvider usersSysLogDataProvider = nacidDataProvider.getUsersDataProvider();
			usersSysLogDataProvider.stopUserSysLogging(se.getSession().getId());
		}
	}

}
