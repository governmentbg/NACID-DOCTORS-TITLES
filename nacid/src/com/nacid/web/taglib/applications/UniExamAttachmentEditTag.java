package com.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.UniExamAttachmentWebModel;

public class UniExamAttachmentEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		UniExamAttachmentWebModel webmodel = (UniExamAttachmentWebModel) getJspContext()
			.getAttribute( WebKeys.UNI_EXAM_ATTCH_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
		
		getJspContext().setAttribute("id", webmodel.getId());
		getJspContext().setAttribute("universityValidityId", webmodel.getDiplomaTypeId());
		getJspContext().setAttribute("docDescr", webmodel.getDocDescr());
		getJspContext().setAttribute("fileName", webmodel.getFileName());
		getJspContext().setAttribute("applicationId", webmodel.getApplicationId());
		getJspContext().setAttribute("groupName", webmodel.getGroupName());
		getJspContext().setAttribute("scannedFileName", webmodel.getScannedFileName());

		getJspContext().setAttribute("docflowNum", webmodel.getDocflowNum());
        getJspContext().setAttribute("docflowUrl", webmodel.getDocflowUrl());

		
		getJspContext().setAttribute("backUrlUniExam", 
				getJspContext().getAttribute("backUrlUniExam", PageContext.SESSION_SCOPE));
		
		
		getJspBody().invoke(null);
	}
}
