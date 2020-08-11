package com.nacid.web.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;


import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.UserAccessUtils;

public class HasUserAccessTag extends SimpleTagSupport {

	private String groupname;
	private String operationname;
	//private Integer groupId;
	//private Integer operationId;
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public void setOperationname(String operationname) {
		this.operationname = operationname;
	}

	public void doTag() throws JspException, IOException {
		User u = (User) getJspContext().getAttribute(WebKeys.LOGGED_USER, PageContext.SESSION_SCOPE);
		Integer groupId;
		Integer webApplicationId = (Integer) ((PageContext)getJspContext()).getServletContext().getAttribute(WebKeys.WEB_APPLICATION_ID);
		if (StringUtils.isEmpty(groupname)) {
			groupId = (Integer) getJspContext().getAttribute(WebKeys.GROUP_ID, PageContext.REQUEST_SCOPE);
		} else {
			groupId = UserAccessUtils.getGroupId(groupname);
		}
		Integer operationId = UserAccessUtils.getOperationId(operationname);
		if (u.hasAccess(groupId, operationId, webApplicationId)) {
			getJspBody().invoke(null);
		}
	}

}
