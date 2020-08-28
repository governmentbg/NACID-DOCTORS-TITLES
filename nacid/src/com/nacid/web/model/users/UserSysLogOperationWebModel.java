package com.nacid.web.model.users;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.users.UserSysLogOperationExtended;
import com.nacid.data.DataConverter;
import com.nacid.web.handlers.impl.users.SyslogHandler;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * User: Georgi
 * Date: 20.8.2020 г.
 * Time: 12:50
 */
@Getter
public class UserSysLogOperationWebModel {
    private String id;
    private String userId;
    private String username;
    private String userFullName;
    private String sessionId;
    private String remoteAddress;
    private String timeLogin;
    private String timeLogout;
    private String groupName;
    private String operationName;
    private String queryString;
    private String dateCreated;
    private String description;

    public UserSysLogOperationWebModel(NacidDataProvider nacidDataProvider, UserSysLogOperationExtended rec) {
        id = rec.getId() + "";
        userId = rec.getUserId() + "";
        username = rec.getUsername();
        userFullName = rec.getUserFullName();
        sessionId = rec.getSessionId();
        remoteAddress = rec.getRemoteAddress();
        timeLogin = DataConverter.formatDateTime(rec.getTimeLogin(), false);
        timeLogout = DataConverter.formatDateTime(rec.getTimeLogout(), false);
        groupName = SyslogHandler.getGroupName(nacidDataProvider, rec.getGroupName());
        operationName = SyslogHandler.getOperationName(rec.getOperationName());
        queryString = rec.getQueryParams().entrySet().stream().map(r -> r.getKey() + "=" + StringUtils.join(r.getValue(), "&")).collect(Collectors.joining("<br />"));
        dateCreated = DataConverter.formatDateTime(rec.getDateCreated(), false);
        description = rec.getDescription();
    }



}
