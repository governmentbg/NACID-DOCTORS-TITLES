package com.ext.nacid.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.web.model.applications.ExtESignedInformationWebModel;
import com.ext.nacid.web.model.applications.report.ExtApplicationForReportWebModel;

public class ApplicationReportESignInformationTag extends SimpleTagSupport {
	 
	
	public void doTag() throws JspException, IOException {
	
		ApplicationReportViewTag parent = (ApplicationReportViewTag)findAncestorWithClass(this, ApplicationReportViewTag.class);
		if (parent == null) {
			return;
		}
		ExtApplicationForReportWebModel webmodel = parent.getWebModel();
		ExtESignedInformationWebModel signedInfoWebModel = webmodel.getEsignedInformation();
		if (signedInfoWebModel != null) {
			
			getJspContext().setAttribute("issuer", signedInfoWebModel.getIssuer());
			getJspContext().setAttribute("name", signedInfoWebModel.getName());
			getJspContext().setAttribute("email", signedInfoWebModel.getEmail());
			getJspContext().setAttribute("civilId", signedInfoWebModel.getCivilId());
			getJspContext().setAttribute("unifiedIdCode", signedInfoWebModel.getUnifiedIdCode());
			getJspContext().setAttribute("validityFrom", signedInfoWebModel.getValidityFrom());
			getJspContext().setAttribute("validityTo", signedInfoWebModel.getValidityTo());
			getJspBody().invoke(null);
		}
		
		
	}
	
}
