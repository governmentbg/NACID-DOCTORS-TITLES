package com.nacid.web.taglib.users;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class UserGroupMembershipColHeadersTag extends SimpleTagSupport {
  public void doTag() throws JspException, IOException {
    for (Integer i:UserGroupMembershipHelper.operationToNameMap.keySet()) {
      getJspContext().setAttribute("columnname", UserGroupMembershipHelper.operationToNameMap.get(i));
      getJspBody().invoke(null);
    }  
  }
}
