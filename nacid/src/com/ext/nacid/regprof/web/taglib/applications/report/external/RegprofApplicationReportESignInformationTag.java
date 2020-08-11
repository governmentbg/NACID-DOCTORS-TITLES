package com.ext.nacid.regprof.web.taglib.applications.report.external;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofApplicationForReportWebModel;
import com.ext.nacid.regprof.web.model.applications.report.ExtRegprofESignedInformationWebModel;

public class RegprofApplicationReportESignInformationTag extends SimpleTagSupport {
	 
	
	public void doTag() throws JspException, IOException {
	
		RegprofApplicationReportViewTag parent = (RegprofApplicationReportViewTag)findAncestorWithClass(this, RegprofApplicationReportViewTag.class);
		if (parent == null) {
			return;
		}
		ExtRegprofApplicationForReportWebModel webmodel = parent.getWebModel();
		ExtRegprofESignedInformationWebModel signedInfoWebModel = webmodel.getEsignedInfo();
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
