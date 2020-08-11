package com.ext.nacid.web.taglib.expert;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.model.expert.ExpertStatementWebModel;
import com.nacid.web.WebKeys;

public class ExpertStatementEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
	    ExpertStatementWebModel webmodel = (ExpertStatementWebModel) getJspContext()
			.getAttribute( WebKeys.EXPERT_STATEMENT_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
		
		getJspContext().setAttribute("id", webmodel.getId());
		getJspContext().setAttribute("docDescr", webmodel.getDocDescr());
		getJspContext().setAttribute("fileName", webmodel.getFileName());
		getJspContext().setAttribute("applicationId", webmodel.getApplicationId());
		
		
		
		getJspBody().invoke(null);
	}
}
