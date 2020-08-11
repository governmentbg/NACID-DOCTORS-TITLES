package com.ext.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.ext.nacid.web.model.applications.ExtApplicationWebModel;

public class ExtApplicationAttachmentsListTag extends SimpleTagSupport {

	public void doTag() throws JspException, IOException {
		
		ExtApplicationWebModel webmodel = (ExtApplicationWebModel) getJspContext().getAttribute(WebKeys.APPLICATION_WEB_MODEL, PageContext.REQUEST_SCOPE);
	    if (webmodel != null) {//tova se polzva za Ext attachments
	        getJspContext().setAttribute("record_id", webmodel.getId());
	    } else { //tova se polzva za ExtRegprof attachments - tam nqma takyv WebModel
	        getJspContext().setAttribute("record_id", getJspContext().getAttribute("id", PageContext.REQUEST_SCOPE));
	    }
	    Boolean isView = (Boolean) getJspContext().getAttribute(WebKeys.OPERATION_VIEW, PageContext.REQUEST_SCOPE);
	    getJspContext().setAttribute("operation",  isView == null || isView ? "view" : "edit");

		getJspBody().invoke(null);
	}

}
