package com.nacid.regprof.web.handlers.impl.inquires;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Company;
import com.nacid.bl.impl.applications.regprof.RegProfApplicationForInquireImpl;
import com.nacid.regprof.web.model.inquires.ApplicantInquireWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.ComboBoxUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ApplicantInquireHandler extends CommonInquireHandler {
	
	public ApplicantInquireHandler(ServletContext servletContext) {
		super(servletContext);
	}

	
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
	    List<Company> list = getNacidDataProvider().getCompanyDataProvider().getCompanies(false);
	    ComboBoxUtils.generateComboBox(null, list, request, "companiesComboBox", true, "getId", "getName");
	    request.setAttribute(WebKeys.NEXT_SCREEN, "applicant_inquire");
	}
	
	protected List<RegProfApplicationForInquireImpl> getApplications(HttpServletRequest request) {
		ApplicantInquireWebModel applicantInquireWebModel = new ApplicantInquireWebModel(request);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		List<RegProfApplicationForInquireImpl> apps = nacidDataProvider.getRegprofInquireDataProvider().getRegprofApplicationsForApplicantInquire(
				applicantInquireWebModel.getApplicantFname(),
				applicantInquireWebModel.getApplicantSname(),
				applicantInquireWebModel.getApplicantLname(),
				applicantInquireWebModel.getApplicantPersonalId(),
				applicantInquireWebModel.getDiplFName(),
                applicantInquireWebModel.getDiplSName(),
                applicantInquireWebModel.getDiplLName(),
                applicantInquireWebModel.getReprFName(),
                applicantInquireWebModel.getReprSName(),
                applicantInquireWebModel.getReprLName(),
                applicantInquireWebModel.getReprPersonalId(),
                applicantInquireWebModel.getRepresentativeCompany(),
				applicantInquireWebModel.getApplicationNum(),
				applicantInquireWebModel.getDateFrom(),
				applicantInquireWebModel.getDateTo()
				);
		return apps;
	}


	protected String getTableName() {
		return "applicantInquireTable";
	}
	
	
	
    
}
