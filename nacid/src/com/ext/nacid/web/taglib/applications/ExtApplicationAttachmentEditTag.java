package com.ext.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.model.applications.ExtApplicationAttachmentWebModel;
import com.nacid.web.WebKeys;

public class ExtApplicationAttachmentEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		ExtApplicationAttachmentWebModel webmodel = (ExtApplicationAttachmentWebModel) 
			getJspContext().getAttribute(WebKeys.EXT_APPLICATION_ATTCH_WEB_MODEL, PageContext.REQUEST_SCOPE);

		getJspContext().setAttribute("id", webmodel.getId());
		getJspContext().setAttribute("applicationId", webmodel.getApplicationId());
		getJspContext().setAttribute("docDescr", webmodel.getDocDescr());
		getJspContext().setAttribute("fileName", webmodel.getFileName());

		getJspContext().setAttribute("backUrlApplAtt", getJspContext().getAttribute("backUrlApplAtt", PageContext.SESSION_SCOPE));

		getJspBody().invoke(null);
	}
}
