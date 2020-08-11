package com.nacid.web.taglib.common;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.common.RadioButtonWebModel;

public class RadioButtonTag extends SimpleTagSupport {
  private String attributeName;
  private String style;
  private String onclick;
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
  
  public void setStyle(String style) {
    this.style = style;
  }

  public void setOnclick(String onclick) {
    this.onclick = onclick;
  }
  
  
  public void doTag() throws JspException, IOException {
    if (attributeName == null || "".equals(attributeName)) {
      attributeName = WebKeys.RADIO_BUTTON;
    }
    RadioButtonWebModel webmodel = (RadioButtonWebModel) getJspContext().getAttribute(attributeName, PageContext.REQUEST_SCOPE);
    if (webmodel != null) {
      String onclck = (onclick == null || "".equals(onclick)) ? "" : "onclick = \"" + onclick + "\" ";
      String stl = (style == null || "".equals(style)) ? "" : "class = \"" + style + "\" ";
      for (int i = 0; i < webmodel.getItemsCount(); i++) {
        getJspContext().setAttribute("key", webmodel.getItemKey(i));
        getJspContext().setAttribute("value", webmodel.getItemValue(i));
        String selected = "checked=\"checked\"";
        getJspContext().setAttribute("selected", webmodel.isItemSelected(i) ? selected : "");
        getJspContext().setAttribute("radioonclick", onclck);
        getJspContext().setAttribute("radiostyle", stl);
        getJspContext().setAttribute("cnt", i);
        getJspContext().setAttribute("additionalAttributes", webmodel.getAdditionalAttributes(i));
        getJspBody().invoke(null);
      }
    }
  }
}
