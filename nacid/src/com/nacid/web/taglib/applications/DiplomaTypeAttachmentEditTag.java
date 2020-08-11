package com.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.DiplomaTypeAttachmentWebModel;

public class DiplomaTypeAttachmentEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		DiplomaTypeAttachmentWebModel webmodel = (DiplomaTypeAttachmentWebModel) getJspContext()
			.getAttribute( WebKeys.DIPLOMA_TYPE_ATTCH_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
		
		getJspContext().setAttribute("id", webmodel.getId());
		getJspContext().setAttribute("diplomaTypeId", webmodel.getDiplomaTypeId());
		getJspContext().setAttribute("docDescr", webmodel.getDocDescr());
		getJspContext().setAttribute("diplomaTypeTitle", webmodel.getDiplomaTypeTitle());
		getJspContext().setAttribute("fileName", webmodel.getFileName());
		
		getJspContext().setAttribute("backUrlDiplType", 
				getJspContext().getAttribute("backUrlDiplType", PageContext.SESSION_SCOPE));
		
		
		getJspBody().invoke(null);
	}
}
