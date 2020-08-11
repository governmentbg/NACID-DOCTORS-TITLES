package com.nacid.web.taglib.common;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.nacid.web.WebKeys;
import com.nacid.web.model.common.SystemMessageWebModel;

public class SystemMessageTag extends SimpleTagSupport {
    private SystemMessageWebModel webmodel;
    private String name;
    private String id;
    public void doTag() throws JspException, IOException {
        
        String paramName = WebKeys.SYSTEM_MESSAGE;
        if(name != null && name.trim().length() != 0) {
            paramName = name;
        }
        
        webmodel = (SystemMessageWebModel) getJspContext().getAttribute(paramName, PageContext.REQUEST_SCOPE);
        if (webmodel != null) {
            getJspContext().setAttribute("title", webmodel.getTitle());
            getJspContext().setAttribute("style", webmodel.getStyle());
            getJspContext().setAttribute("divId", StringUtils.isEmpty(id) ? "" : " id=\"" + id + "\" ");
            getJspBody().invoke(null);
        }
    }

    public SystemMessageWebModel getWebModel() {
        return webmodel;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
}
