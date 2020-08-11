package com.ext.nacid.web.taglib.expert;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;

public class ExpertApplicationFormHeaderTag extends SimpleTagSupport {
    private int formid;
    
    public void setFormid(int formid) {
        this.formid = formid;
    }

    @Override
    public void doTag() throws JspException, IOException {
        
        
        int activeFormId = (Integer)getJspContext().getAttribute(WebKeys.EXPERT_APPLICATION_FORM_ID, PageContext.REQUEST_SCOPE); 
        
        if (formid == activeFormId) {
            getJspContext().setAttribute("formHeaderClass", "selected");
            getJspContext().setAttribute("formDivStyle", "display:block;");
        } else {
            getJspContext().setAttribute("formHeaderClass", "");
            getJspContext().setAttribute("formDivStyle", "display:none;");
        }
        getJspBody().invoke(null);
    }
}
