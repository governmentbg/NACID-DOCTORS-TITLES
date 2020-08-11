package com.nacid.web.taglib.common;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
/**
 * @author ggeorgiev
 * "submit" butona se vijda samo kogato se editva forma, a kogato se razglejda ne se vijda!
 */
public class SubmitTag extends SimpleTagSupport {
    private String onclick;
    private String type;
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public void setType(String type) {
        this.type = "".equals(type) || type == null ? "submit" : type;
    }

    public void doTag() throws JspException, IOException {
        Boolean operationView = (Boolean) getJspContext().getAttribute(WebKeys.OPERATION_VIEW, PageContext.REQUEST_SCOPE);
        if (operationView == null || operationView == false) {
            getJspContext().setAttribute("onclck", onclick == null || "".equals(onclick) ? " " : " onclick=\"" + onclick + "\" ");
            getJspContext().setAttribute("tp", type);
            getJspBody().invoke(null);
        }
    }
}
