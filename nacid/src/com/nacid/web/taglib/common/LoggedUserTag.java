package com.nacid.web.taglib.common;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.bl.users.User;
import com.nacid.web.WebKeys;

public class LoggedUserTag extends SimpleTagSupport {
  public void doTag() throws JspException, IOException {
    User user = (User) getJspContext().getAttribute(WebKeys.LOGGED_USER, PageContext.SESSION_SCOPE);
    if (user != null) {
      getJspContext().setAttribute("username", user.getFullName());
      getJspBody().invoke(null);
    }
  }
}
