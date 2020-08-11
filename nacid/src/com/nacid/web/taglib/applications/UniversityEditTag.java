package com.nacid.web.taglib.applications;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.UniversityWebModel;

public class UniversityEditTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		UniversityWebModel webmodel = (UniversityWebModel) getJspContext()
			.getAttribute( WebKeys.UNIVERSITY_WEB_MODEL, PageContext.REQUEST_SCOPE);
		Boolean operationView = (Boolean) getJspContext().getAttribute( WebKeys.OPERATION_VIEW, PageContext.REQUEST_SCOPE);
		if (operationView == null) {
			operationView = false;
		}
		getJspContext().setAttribute("id", webmodel.getId());
		getJspContext().setAttribute("bgName", webmodel.getBgName());
		getJspContext().setAttribute("orgName", webmodel.getOrgName());
		getJspContext().setAttribute("city", webmodel.getCity());
		getJspContext().setAttribute("addrDetails", webmodel.getAddrDetails());
		getJspContext().setAttribute("phone", webmodel.getPhone());
		getJspContext().setAttribute("fax", webmodel.getFax());
		getJspContext().setAttribute("email", webmodel.getEmail());
		getJspContext().setAttribute("webSite", webmodel.getWebSite());
		if (operationView && !StringUtils.isEmpty(webmodel.getWebSite())) {
			getJspContext().setAttribute("webSiteLink", "<a href=\"" + webmodel.getWebSite() + "\" target=\"_blank\">Отвори страницата</a>");
		}
		getJspContext().setAttribute("urlDiplomaRegister", webmodel.getUrlDiplomaRegister());
		if (operationView) {
			getJspContext().setAttribute("urlDiplomaRegisterLink", webmodel.getUrlDiplomaRegisterLink());
		}
		getJspContext().setAttribute("dateFrom", webmodel.getDateFrom());
		getJspContext().setAttribute("dateTo", webmodel.getDateTo());
		
		getJspBody().invoke(null);
	}
}
