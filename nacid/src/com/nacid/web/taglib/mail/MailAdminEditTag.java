package com.nacid.web.taglib.mail;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.mail.MailWebModel;

public class MailAdminEditTag extends SimpleTagSupport {
    @Override
    public void doTag() throws JspException, IOException {
        MailWebModel webModel = (MailWebModel)getJspContext().getAttribute(WebKeys.MAIL_WEB_MODEL, PageContext.REQUEST_SCOPE);
        
        getJspContext().setAttribute("id", webModel.getId());
        getJspContext().setAttribute("body", webModel.getBody());
        getJspContext().setAttribute("subject", webModel.getSubject());
        getJspContext().setAttribute("date", webModel.getDate());
        getJspContext().setAttribute("recipient", webModel.getRecipient());
        getJspContext().setAttribute("type", webModel.getType());
        getJspContext().setAttribute("status", webModel.getStatus());
        
        getJspBody().invoke(null);
    }
}
