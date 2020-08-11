package com.nacid.web.taglib.common;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.model.common.SystemMessageWebModel;

public class SystemMessageAttributeTag extends SimpleTagSupport {
  public void doTag() throws JspException, IOException {
    SystemMessageTag parent = (SystemMessageTag) getParent();
    SystemMessageWebModel webmodel = parent.getWebModel();
    if (webmodel != null && webmodel.hasAttributes()) {
      List<String> attributes = webmodel.getAttributes();
      for (String s:attributes) {
        getJspContext().setAttribute("attribute", s);
        getJspBody().invoke(null);
      }
    }
  }
}
