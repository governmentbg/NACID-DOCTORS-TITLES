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
        queryString = splitQuery(rec.getQueryString()).entrySet().stream().map(r -> r.getKey() + "=" + StringUtils.join(r.getValue(), "&")).collect(Collectors.joining("<br />"));
        dateCreated = DataConverter.formatDateTime(rec.getDateCreated(), false);
        description = null;
    }

    public Map<String, List<String>> splitQuery(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return new HashMap<>();
        }
        return Arrays.stream(queryString.split("&"))
                .map(this::splitQueryParameter)
                .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
    }

    public AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        try {
            final int idx = it.indexOf("=");
            final String key = idx > 0 ? it.substring(0, idx) : it;
            final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
            return new AbstractMap.SimpleImmutableEntry<>(
                    key == null ? "" : URLDecoder.decode(key, "UTF-8"),
                    value == null ? "" : URLDecoder.decode(value, "UTF-8")
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
