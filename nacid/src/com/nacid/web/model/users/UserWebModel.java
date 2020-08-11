package com.nacid.web.model.users;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nacid.bl.users.User;
import com.nacid.bl.users.UserGroupMembershipForEdit;
import com.nacid.bl.users.UserUpdater;
import com.nacid.web.handlers.impl.users.UsersHandler;

public class UserWebModel {
    private String id;
    private String fullName;
    private String shortName;
    private String userName;
    private String pass;
    private boolean status;
    private String email;
    private String phone;
    private Map<Integer, Map<Integer, Set<Integer>>> userGroupMemberships;

    public UserWebModel(String id, String fullName, String shortName, String userName, String pass, int status, String email, String phone,
            Map<Integer, Map<Integer, Set<Integer>>> userGroupMemberships) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.userName = userName;
        this.pass = pass;
        this.status = status == User.USER_STATUS_ACTIVE ? true : false;
        this.email = email;
        this.phone = phone;
        this.userGroupMemberships = userGroupMemberships;
    }
    public UserWebModel(UserUpdater user) {
        id = user.getUserId() + "";
        fullName = user.getFullName();
        shortName = user.getShortName();
        userName = user.getUserName();
        pass = user.getUserPass();
        status = user.getStatus() == User.USER_STATUS_ACTIVE ? true : false;
        phone = user.getTelephone();
        email = user.getEmail();
        userGroupMemberships = new LinkedHashMap<Integer, Map<Integer,Set<Integer>>>();
        for (Integer app:UsersHandler.SUPPORTED_APPS) {
            List<? extends UserGroupMembershipForEdit> ugms = user.getUserGroupMemberships(app);
            Map<Integer, Set<Integer>> m = new HashMap<Integer, Set<Integer>>();
            userGroupMemberships.put(app, m);
            if (ugms != null) {
                for (UserGroupMembershipForEdit ugm : ugms) {
                    m.put(ugm.getGroupId(), ugm.getOperationIds());  
                }    
            }
        }
    }
    public String getUserName() {
        return userName;
    }
    public String getPass() {
        return pass;
    }
    public String getFullName() {
        return fullName;
    }
    public String getShortName() {
        return shortName;
    }
    public boolean getStatus() {
        return status;
    }
    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public boolean hasAccess(int applicationId, int groupId, int operationId) {
        Set<Integer> usm = userGroupMemberships == null ? null : userGroupMemberships.get(applicationId) == null ? null : userGroupMemberships.get(applicationId).get(groupId);
        if (usm == null) {
            return false;
        }
        return usm.contains(operationId);
    }
}
