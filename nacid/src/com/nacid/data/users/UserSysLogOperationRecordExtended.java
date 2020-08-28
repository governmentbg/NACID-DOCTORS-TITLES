package com.nacid.data.users;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * User: Georgi
 * Date: 20.8.2020 г.
 * Time: 9:25
 */
@Data
@NoArgsConstructor
public class UserSysLogOperationRecordExtended {
    private int id;
    private int userId;
    private String username;
    private String userFullName;
    private String sessionId;
    private String remoteAddress;
    private String remoteHost;
    private Timestamp timeLogin;
    private Timestamp timeLogout;
    private int webApplicationId;
    private String groupName;
    private String operationName;
    private String queryString;
    private Timestamp dateCreated;
    private String description;
}
