package com.nacid.web.taglib.applications;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.ApplicationWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ApplicationAttachmentsListTag extends SimpleTagSupport {

	public void doTag() throws JspException, IOException {
		
		ApplicationWebModel webmodel = (ApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
	    
		getJspContext().setAttribute("record_id", webmodel.getId());
		getJspContext().setAttribute("application_header", webmodel.getApplicationHeader());
		getJspContext().setAttribute("operation", webmodel.getOperationType());
		getJspContext().setAttribute("application_type", webmodel.getApplicationType());

		getJspBody().invoke(null);
	}

}
