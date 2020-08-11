package com.ext.nacid.web.taglib.applications.report.internal;

import com.ext.nacid.web.taglib.applications.report.base.ApplicationReportApplicationBaseViewTag;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.internal.ApplicationInternalForReportWebModel;
import com.nacid.web.model.applications.report.internal.TrainingCourseInternalForReportWebModel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class ApplicationReportViewTag extends ApplicationReportApplicationBaseViewTag {
	ApplicationInternalForReportWebModel webmodel;

	@Override
	public void doTag() throws JspException, IOException {
		super.generateBaseDate();
		
		webmodel = (ApplicationInternalForReportWebModel) getJspContext().getAttribute(attributePrefix + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, PageContext.REQUEST_SCOPE);
		if (webmodel == null) {
			return;
		}
		getJspContext().setAttribute("is_official_email_communication", webmodel.isOfficialEmailCommunication());
		getJspContext().setAttribute("home_is_bg",webmodel.isHomeIsBg());
		getJspContext().setAttribute("show_representative", webmodel.isShowRepresentative());
		getJspContext().setAttribute("is_bg_address_onwer",webmodel.isBgAddressOwnerChecked());
		getJspContext().setAttribute("reprPhone", webmodel.getReprPhone());
        getJspContext().setAttribute("reprCountry", webmodel.getRepresentativeCountry());
		getJspContext().setAttribute("reprAddressDetails", webmodel.getReprAddressDetails());
		getJspContext().setAttribute("reprPcode", webmodel.getReprPcode());
		getJspContext().setAttribute("reprCity", webmodel.getReprCity());
		getJspContext().setAttribute("reprCompany", webmodel.getCompany() == null ? "" : webmodel.getCompany().getName());
		getJspContext().setAttribute("application_status", webmodel.getApplicationStatus());
		getJspContext().setAttribute("application_status_for_applicant", webmodel.getApplicationStatusForApplicant());
		getJspContext().setAttribute("archive_number", webmodel.getArchiveNumber());
		getJspContext().setAttribute("applicantInfo", webmodel.getApplicantInfo());
		getJspContext().setAttribute("submittedDocs", webmodel.getSubmittedDocs());
		getJspContext().setAttribute("show_archive_number", webmodel.showArchiveNumber());
		
		getJspContext().setAttribute("uni_validity_place_recognized", webmodel.getTrainingCourseWebModel().isRecognized());
		getJspContext().setAttribute("is_representative_authorized", webmodel.isRepresentativeAuthorized());
		getJspContext().setAttribute("certificate_number", webmodel.getCertificateNumber());
		
		TrainingCourseInternalForReportWebModel tcwb = webmodel.getTrainingCourseWebModel();

		getJspContext().setAttribute("recognized_and_contains_protocol", webmodel.isRecognizedAndContainsProtocol());
		getJspContext().setAttribute("recognized_specialities", tcwb.getRecognizedSpecialities());
		getJspContext().setAttribute("recognized_qualification", tcwb.getRecognizedQualification());
		getJspContext().setAttribute("recognized_edulevel", tcwb.getRecognizedEduLevel());
		getJspContext().setAttribute("doctorateApplication", webmodel.isDoctorateApplication());

		
		getJspBody().invoke(null);
	}

	public ApplicationInternalForReportWebModel getWebModel() {
		return webmodel;
	}


}
