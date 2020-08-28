package com.nacid.bl.users;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Georgi
 * Date: 20.8.2020 г.
 * Time: 10:12
 */
public interface UserSysLogOperationExtended {
    int getId();

    int getUserId();

    String getUsername();

    String getUserFullName();

    String getSessionId();

    String getRemoteAddress();

    String getRemoteHost();

    Date getTimeLogin();

    Date getTimeLogout();

    int getWebApplicationId();

    String getGroupName();

    String getOperationName();

    String getQueryString();

    Date getDateCreated();

    String getDescription();

    Map<String, List<String>> getQueryParams();
}
