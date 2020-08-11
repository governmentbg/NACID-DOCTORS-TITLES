package com.nacid.bl.impl.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nacid.bl.users.UserGroupMembership;
import com.nacid.bl.users.UsersGroupMembership;
import com.nacid.data.users.UserGroupMembershipRecord;

public class UserGroupsMembershipImpl implements UsersGroupMembership{
    //private Map<Integer, UserGroupMembership> userGroupMemberships = new HashMap<Integer, UserGroupMembership>();
    //private Set<Integer> userWebApplicationsAccess = new HashSet<Integer>();
    private List<UserGroupMembershipImpl> userGroupMemberships = new ArrayList<UserGroupMembershipImpl>();
    public UserGroupsMembershipImpl(List<? extends UserGroupMembershipRecord> records) {
        if (records != null && records.size() > 0) {
            for (UserGroupMembershipRecord r : records) {
                userGroupMemberships.add(new UserGroupMembershipImpl(r));
                //userGroupMemberships.put(r.getGroupId(), new UserGroupMembershipImpl(r));
                //userWebApplicationsAccess.add(r.getWebApplication());
            }
        }
    }
    public boolean hasAccess(int groupId, int operationId, int webApplicationId) {
        /*if (!userWebApplicationsAccess.contains(webApplicationId)) {
        return false;
        }
        if (userGroupMemberships.containsKey(UserGroupMembershipRecord.FULL_ACCESS_GROUP_ID)) {
          return true;
        }
        UserGroupMembership ugm = userGroupMemberships.get(groupId);
        if (ugm == null) {
          return false;
        }*/
        if (userGroupMemberships != null && !userGroupMemberships.isEmpty()) {
            for (UserGroupMembership ugm : userGroupMemberships) {
                if (ugm.getGroupId() == UserGroupMembershipRecord.FULL_ACCESS_GROUP_ID && ugm.getWebApplicationId() == webApplicationId) {
                    return true;
                } else if (ugm.getGroupId() == groupId && ugm.getWebApplicationId() == webApplicationId) {
                    return ugm.hasAccess(operationId);
                }
            }
        }
        // ugm.hasAccess(operationId);
        return false;
    }
}
