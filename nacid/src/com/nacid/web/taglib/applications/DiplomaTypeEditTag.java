package com.nacid.web.taglib.applications;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.DiplomaTypeWebModel;
import com.nacid.web.taglib.FormInputUtils;

public class DiplomaTypeEditTag extends SimpleTagSupport {

	
	private DiplomaTypeWebModel webmodel;
	public void doTag() throws JspException, IOException {
		webmodel = (DiplomaTypeWebModel) getJspContext()
			.getAttribute( WebKeys.DIPLOMA_TYPE_WEB_MODEL, PageContext.REQUEST_SCOPE);
    
		
		getJspContext().setAttribute("id", webmodel.getId());
		getJspContext().setAttribute("type", webmodel.getType());
		getJspContext().setAttribute("numberFormatDescr", webmodel.getNumberFormatDescr());
		getJspContext().setAttribute("protectionElementsDescr", webmodel.getProtectionElementsDescr());
		getJspContext().setAttribute("visualElementsDescr", webmodel.getVisualElementsDescr());
		getJspContext().setAttribute("notes", webmodel.getNotes());
		getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
		getJspContext().setAttribute("dateTo", webmodel.getDateTo());
		getJspContext().setAttribute("title", webmodel.getTitle());
		getJspContext().setAttribute("joint_degree_checked", FormInputUtils.getCheckBoxCheckedText(webmodel.isJointDegree()));
		getJspContext().setAttribute("joint_degree_disabled", webmodel.isJointDegree() ? " disabled=\"disabled\" " : "");
		getJspContext().setAttribute("add_universities_display", webmodel.isJointDegree() ? "" : " style=\"display:none;\" ");
		
		getJspContext().setAttribute("universities_count", webmodel.getDiplomaTypeIssuers().size());
		
		
		getJspBody().invoke(null);
	}
	public DiplomaTypeWebModel getWebmodel() {
		return webmodel;
	}
}
