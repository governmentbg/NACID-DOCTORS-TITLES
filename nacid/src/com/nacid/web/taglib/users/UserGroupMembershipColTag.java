package com.nacid.web.taglib.users;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.data.users.UserGroupMembershipRecord;
import com.nacid.web.taglib.FormInputUtils;

public class UserGroupMembershipColTag extends SimpleTagSupport {
  private UserGroupMembershipRowTag parent;
  
  public void doTag() throws JspException, IOException {
    parent = (UserGroupMembershipRowTag) getParent();
    if (parent == null) {
      return;
    }
    /**
     * ako groupID-to e = -1, togava trqbva da se generira samo 1 checkbox s operation_id = -1
     * i sled tova <td></td> za vseki edin drug groupId
     */
    if (parent.getGroupId() == UserGroupMembershipRecord.FULL_ACCESS_GROUP_ID) {
      StringWriter writer = new StringWriter();
      writeToContext(UserGroupMembershipRecord.FULL_ACCESS_OPERATION_ID, writer);
      for (int i = 0; i < UserGroupMembershipHelper.operationToNameMap.keySet().size() - 1; i++) {
        writer.append("<td>&nbsp;</td>");
      }
      getJspContext().getOut().write(writer.toString());
    } else {
      for (Integer i:UserGroupMembershipHelper.operationToNameMap.keySet()) {
        writeToContext(i, null);
      }  
    }
    
  }
  private void writeToContext(int operationId, Writer writer) throws JspException, IOException {
    getJspContext().setAttribute("group_id", parent.getGroupId());
    getJspContext().setAttribute("operation_id", operationId);
    getJspContext().setAttribute("checked", FormInputUtils.getCheckBoxCheckedText(parent.hasAccess(operationId)));
    getJspBody().invoke(writer);
  }
  
}
