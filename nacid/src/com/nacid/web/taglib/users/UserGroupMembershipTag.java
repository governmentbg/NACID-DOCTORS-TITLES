package com.nacid.web.taglib.users;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.NacidDataProvider;
import com.nacid.web.handlers.impl.users.UsersHandler;
import com.nacid.web.model.users.UserWebModel;

public class UserGroupMembershipTag extends SimpleTagSupport {
    private static final Map<Integer, String> applicationNames = new LinkedHashMap<Integer, String>();
    static {
        
        applicationNames.put(NacidDataProvider.APP_NACID_ID, "RUDi");
        applicationNames.put(NacidDataProvider.APP_NACID_REGPROF_ID, "NoRQ");
        applicationNames.put(NacidDataProvider.APP_NACID_EXT_ID, "TODO");
        applicationNames.put(NacidDataProvider.APP_NACID_REGPROF_EXT_ID, "TODO");
    }
    
    private int applicationId;
    public void doTag() throws JspException, IOException {
        UserEditTag parent = (UserEditTag) findAncestorWithClass(this, UserEditTag.class);
        if (parent == null) {
            return;
        }
        Set<Integer> apps = UsersHandler.SUPPORTED_APPS;//webmodel.getApplcationIds();
        if (apps != null) {
            for (Integer a:apps) {
                applicationId = a;
                getJspContext().setAttribute("application_id", getApplicationId());
                getJspContext().setAttribute("application_name", applicationNames.get(getApplicationId()));
                getJspBody().invoke(null);
            }
        }
    }
    public Integer getApplicationId() {
        return applicationId;
    }
    

}
