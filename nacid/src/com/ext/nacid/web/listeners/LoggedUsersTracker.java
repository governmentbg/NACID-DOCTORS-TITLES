package com.ext.nacid.web.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
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
		/*User user = (User) se.getSession().getAttribute(WebKeys.LOGGED_USER);
		if (user != null) {
			NacidDataProvider nacidDataProvider = BaseRequestHandler.getNacidDataProvider(se.getSession());
			UsersDataProvider usersSysLogDataProvider = nacidDataProvider.getUsersDataProvider();
			usersSysLogDataProvider.stopUserSysLogging(se.getSession().getId());
		}*/
	}

}
