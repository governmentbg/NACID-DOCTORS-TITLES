package com.nacid.web.taglib.users;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.users.ExtUserWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class ExtUserEditTag extends SimpleTagSupport {
    private ExtUserWebModel webmodel;

    public void doTag() throws JspException, IOException {
        webmodel = (ExtUserWebModel) getJspContext().getAttribute(WebKeys.EXT_USER_WEB_MODEL, PageContext.REQUEST_SCOPE);
        getJspContext().setAttribute("id", webmodel == null ? "0" : webmodel.getId());
        getJspContext().setAttribute("username", webmodel == null ? "" : webmodel.getUserName());
        getJspContext().setAttribute("fullname", webmodel == null ? "" : webmodel.getFullName());
        getJspContext().setAttribute("type", webmodel == null ? "" : webmodel.getType());
        getJspContext().setAttribute("status_checked", FormInputUtils.getCheckBoxCheckedText(webmodel == null ? false : webmodel.getStatus()));
        getJspContext().setAttribute("email", webmodel.getEmail());
        getJspBody().invoke(null);
    }

}
