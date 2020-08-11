package com.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.DiplExamAttachmentWebModel;

public class DiplExamAttachmentEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		DiplExamAttachmentWebModel webmodel = (DiplExamAttachmentWebModel) getJspContext()
			.getAttribute( WebKeys.DIPL_EXAM_ATTCH_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
		
		getJspContext().setAttribute("id", webmodel.getId());
		getJspContext().setAttribute("diplExamId", webmodel.getDiplExamId());
		getJspContext().setAttribute("docDescr", webmodel.getDocDescr());
		getJspContext().setAttribute("fileName", webmodel.getFileName());
		getJspContext().setAttribute("applicationId", webmodel.getApplicationId());
		getJspContext().setAttribute("scannedFileName", webmodel.getScannedFileName());

		getJspContext().setAttribute("docflowNum", webmodel.getDocflowNum());
        getJspContext().setAttribute("docflowUrl", webmodel.getDocflowUrl());

		
		getJspContext().setAttribute("backUrlDiplExam", 
				getJspContext().getAttribute("backUrlDiplExam", PageContext.SESSION_SCOPE));
		
		
		getJspBody().invoke(null);
	}
}
