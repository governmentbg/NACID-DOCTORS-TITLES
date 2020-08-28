package com.nacid.bl.impl.users;

import com.nacid.bl.users.UserSysLogOperationExtended;
import com.nacid.data.users.UserSysLogOperationRecordExtended;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * User: Georgi
 * Date: 27.8.2020 г.
 * Time: 16:08
 */
@Getter
@Setter
public class UserSysLogOperationExtendedImpl implements UserSysLogOperationExtended {
    private int id;
    private int userId;
    private String username;
    private String userFullName;
    private String sessionId;
    private String remoteAddress;
    private String remoteHost;
    private Date timeLogin;
    private Date timeLogout;
    private int webApplicationId;
    private String groupName;
    private String operationName;
    private String queryString;
    private Date dateCreated;
    private String description;
    private Map<String, List<String>> queryParams;

    public UserSysLogOperationExtendedImpl(UserSysLogOperationRecordExtended rec) {
        this.id = rec.getId();
        this.userId = rec.getUserId();
        this.username = rec.getUsername();
        this.userFullName = rec.getUserFullName();
        this.sessionId = rec.getSessionId();
        this.remoteAddress = rec.getRemoteAddress();
        this.remoteHost = rec.getRemoteHost();
        this.timeLogin = rec.getTimeLogin();
        this.timeLogout = rec.getTimeLogout();
        this.webApplicationId = rec.getWebApplicationId();
        this.groupName = rec.getGroupName();
        this.operationName = rec.getOperationName();
        this.queryString = rec.getQueryString();
        this.dateCreated = rec.getDateCreated();
        this.description = rec.getDescription();
        this.queryParams = _generateQueryParams();
    }

    public List<String> getParamValues(String paramName) {
        return queryParams.get(paramName);
    }
    public String getParamValue(String paramName) {
        List<String> res = queryParams.get(paramName);
        return  res == null || res.size() < 1 ? null : res.get(0);
    }

    private Map<String, List<String>> _generateQueryParams() {
        if (StringUtils.isEmpty(queryString)) {
            return new HashMap<>();
        }
        return Arrays.stream(queryString.split("&"))
                .map(this::splitQueryParameter)
                .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, mapping(Map.Entry::getValue, toList())));
    }

    private AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
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
