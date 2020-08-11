package com.ext.nacid.web.taglib.applications.report.external;

import com.ext.nacid.web.model.applications.report.ExtApplicationForReportWebModel;
import com.ext.nacid.web.taglib.applications.report.base.ApplicationReportApplicationBaseViewTag;
import com.nacid.web.WebKeys;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class ApplicationReportViewTag extends ApplicationReportApplicationBaseViewTag {
	ExtApplicationForReportWebModel webmodel;

	@Override
	public void doTag() throws JspException, IOException {
		super.generateBaseDate();
		
		webmodel = (ExtApplicationForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
		if (webmodel == null) {
			return;
		}
        getJspContext().setAttribute("applicant_type", webmodel.getApplicantType());

		getJspBody().invoke(null);
	}

	public ExtApplicationForReportWebModel getWebModel() {
		return webmodel;
	}


}
