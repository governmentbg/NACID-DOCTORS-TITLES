package com.nacid.web.taglib.users;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.users.UserWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class UserEditTag extends SimpleTagSupport {
  private UserWebModel webmodel;
  public void doTag() throws JspException, IOException {
    webmodel = (UserWebModel) getJspContext().getAttribute( WebKeys.USER, PageContext.REQUEST_SCOPE);
    getJspContext().setAttribute("id", webmodel == null ? "0" : webmodel.getId());
    getJspContext().setAttribute("username", webmodel == null ? "" : webmodel.getUserName());
    getJspContext().setAttribute("fullname", webmodel == null ? "" : webmodel.getFullName());
    getJspContext().setAttribute("shortname", webmodel == null ? "" : webmodel.getShortName());
    getJspContext().setAttribute("password", /*webmodel == null ? "" : webmodel.getPass()*/"");
    getJspContext().setAttribute("status_checked", FormInputUtils.getCheckBoxCheckedText(webmodel == null ? false : webmodel.getStatus()));
    getJspContext().setAttribute("email", webmodel == null ? "" : webmodel.getEmail());
    getJspContext().setAttribute("phone", webmodel == null ? "" : webmodel.getPhone());
    getJspBody().invoke(null);
  }
  public UserWebModel getWebmodel() {
    return webmodel;
  }

}
