package com.ext.nacid.web.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.ext.nacid.web.model.ExtPersonWebModel;

public class ExtPersonEditTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        ExtPersonWebModel webmodel = (ExtPersonWebModel) getJspContext()
            .getAttribute( WebKeys.EXT_PERSON_WEB_MODEL, PageContext.REQUEST_SCOPE);

    
        getJspContext().setAttribute("personalIdTypes", webmodel.getPersonalIdTypes()); 
        getJspContext().setAttribute("personalId", webmodel.getPersonalId());
        getJspContext().setAttribute("fname", webmodel.getFname());
        getJspContext().setAttribute("sname", webmodel.getSname());
        getJspContext().setAttribute("lname", webmodel.getLname());
        getJspContext().setAttribute("birthCity", webmodel.getBirthCity());
        getJspContext().setAttribute("birthDate", 
                webmodel.getBirthDate());
        getJspContext().setAttribute("email", webmodel.getEmail());
        getJspContext().setAttribute("username", webmodel.getUsername());
        
        getJspBody().invoke(null);
    }
}
