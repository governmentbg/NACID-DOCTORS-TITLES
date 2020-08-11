package com.ext.nacid.web.handlers.impl.applications;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.ext.nacid.web.model.applications.report.ExtApplicationForReportWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationAttachmentDataProvider;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.model.applications.report.base.AttachmentForReportBaseWebModel;
import com.nacid.web.model.applications.report.internal.ApplicationInternalForReportWebModel;

public class ApplicantReportHandler extends NacidExtBaseRequestHandler {

    public ApplicantReportHandler(ServletContext servletContext) {
        super(servletContext);
    }

	/**
	 * ApplicantReportHandler(ApplicantReportAttachmentsHandler) raboti s vyn6noto application ID, 
	 * dokato ExpertReportHandler(ExpertReportAttachmentsHandler) raboti s vytre6noto application ID!!! 
	 */
    @Override
	public void handleView(HttpServletRequest request, HttpServletResponse response) {
		int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		ExtApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
		ExtApplication application = applicationId == 0 ? null : applicationsDataProvider.getApplication(applicationId);
		if (application == null) {
			throw new RuntimeException("No application is set....");
		}
		
		User user = getLoggedUser(request, response);
		UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_VIEW, user, application, nacidDataProvider);
		
		
		generateExternalApplicantReport(nacidDataProvider, application, request, "ext");
		setNextScreen(request, "report_applicant");
	}
    /**
     * generira applicantReport sprqmo internal application
     * @param nacidDataProvider
     * @param application
     * @param request
     * @param attrPrefix - shte se polzva za prefix na atributa, t.k. ima stranici (zasega edna edinstvena, v koqto trqbva da se vijdat 2 reporta ednovremenno i atributite trqbva da sa s razlichni imena)
     */
	public static void generateInternalApplicantReport(NacidDataProvider nacidDataProvider, Application application, HttpServletRequest request, String attrPrefix) {
		//ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		AttachmentDataProvider applicationAttachmentDataProvider = nacidDataProvider.getApplicationAttachmentDataProvider();
		List<Attachment> attachments = applicationAttachmentDataProvider.getAttachmentsForParent(application.getId());
		if (attachments != null && attachments.size() > 0) {
			List<AttachmentForReportBaseWebModel> attachmentsWebModel = new ArrayList<AttachmentForReportBaseWebModel>();
			for (Attachment a:attachments) {
				attachmentsWebModel.add(new AttachmentForReportBaseWebModel(a));
			}
			request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, attachmentsWebModel);
		}
		request.setAttribute((attrPrefix == null ? "" : attrPrefix) + "applicationId", application.getId());
		request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL, new ApplicationInternalForReportWebModel(application));
	}
	/**
	 * generira applicantReport sprqmo external application
	 * @param nacidDataProvider
	 * @param application
	 * @param request
	 */
	public static void generateExternalApplicantReport(NacidDataProvider nacidDataProvider, ExtApplication application, HttpServletRequest request, String attrPrefix) {
		//ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		ExtApplicationAttachmentDataProvider applicationAttachmentDataProvider = nacidDataProvider.getExtApplicationAttachmentDataProvider();
		List<Attachment> attachments = applicationAttachmentDataProvider.getAttachmentsForApplication(application.getId());
		if (attachments != null && attachments.size() > 0) {
			List<AttachmentForReportBaseWebModel> attachmentsWebModel = new ArrayList<AttachmentForReportBaseWebModel>();
			for (Attachment a:attachments) {
				attachmentsWebModel.add(new AttachmentForReportBaseWebModel(a));
			}
			request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, attachmentsWebModel);
		}
		request.setAttribute((attrPrefix == null ? "" : attrPrefix) + "applicationId", application.getId());
		request.setAttribute((attrPrefix == null ? "" : attrPrefix) + WebKeys.APPLICATION_FOR_REPORT_WEB_MODEL,  new ExtApplicationForReportWebModel(application, nacidDataProvider.getExtTrainingCourseDataProvider()));
	}
}
