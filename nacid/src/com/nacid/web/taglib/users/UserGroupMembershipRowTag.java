package com.nacid.web.taglib.users;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.data.users.UserGroupMembershipRecord;
import com.nacid.web.model.users.UserWebModel;

public class UserGroupMembershipRowTag extends SimpleTagSupport {
    private static final Map<Integer, String> userGroupNames = new LinkedHashMap<Integer, String>();
    static {
        userGroupNames.put(UserGroupMembershipRecord.FULL_ACCESS_GROUP_ID, "Всички");
        userGroupNames.put(1, "Администриране");
        userGroupNames.put(2, "Номенклатури");
        userGroupNames.put(3, "Заявления");
        userGroupNames.put(4, "Справки");
    }
    private UserWebModel webmodel;
    private int groupId;
    private int applicationId;

    public void doTag() throws JspException, IOException {
        UserEditTag userEditTag = (UserEditTag) findAncestorWithClass(this, UserEditTag.class);
        if (userEditTag == null) {
            return;
        }
        webmodel = userEditTag.getWebmodel();
        UserGroupMembershipTag parent = (UserGroupMembershipTag) findAncestorWithClass(this, UserGroupMembershipTag.class);
        applicationId = parent.getApplicationId();
        
        for (Integer i:userGroupNames.keySet()) {
            groupId = i;
            getJspContext().setAttribute("name", getGroupName());
            getJspBody().invoke(null);  
        }

    }
    public boolean hasAccess(int operationId) {
        if (webmodel == null) {
            return false;
        }
        return webmodel.hasAccess(applicationId, groupId, operationId);
    }
    public String getGroupName() {
        return userGroupNames.get(groupId);
    }
    public int getGroupId() {
        return groupId;
    }
   
    

}
