package com.ext.nacid.web.handlers.impl.applications;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationAttachmentDataProvider;
import com.nacid.bl.external.applications.ExtApplicationsDataProvider;
//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.exceptions.UnknownRecordException;


public class ApplicantReportExternalAttachmentsHandler extends NacidExtBaseRequestHandler {
	public ApplicantReportExternalAttachmentsHandler(ServletContext servletContext) {
		super(servletContext);
	}
	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
		//id-to na vytre6niq application
		Integer applicationId = DataConverter.parseInteger(request.getParameter("application_id"), null);
		//idto na vytreshniq attachment document
		Integer attachmentId = DataConverter.parseInteger(request.getParameter("attachment_id"), null);
		int type = DataConverter.parseInt(request.getParameter("att_type"), 0);
		if (applicationId == null || attachmentId == null ) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		if (type == 0) {
			throw new RuntimeException("Unknown attachment type id(" + type + "). The possible types are defined in ExpertReportAttachmentsHandler...");
		}
		
		NacidDataProvider nacidDataProvider = getNacidDataProvider();
		User user = getLoggedUser(request, response);

		ExtApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getExtApplicationsDataProvider();
		ExtApplication application = applicationId == null ? null : applicationsDataProvider.getApplication(applicationId);
		if (application == null) {
			throw new UnknownRecordException("unknown application id..." + request.getParameter("application_id"));
		}
		UserAccessUtils.checkApplicantActionAccess(UserAccessUtils.USER_ACTION_VIEW, user, application, nacidDataProvider);
		ExtApplicationAttachmentDataProvider extApplicationAttachmentDataProvider = nacidDataProvider.getExtApplicationAttachmentDataProvider();
		Attachment att = extApplicationAttachmentDataProvider.getApplicationAttacment(attachmentId, true);
		if (att == null) {
			throw new UnknownRecordException("unknown attachment id..." + request.getParameter("attachment_id"));
		}
		if (att.getParentId() != application.getId()) {
			throw new RuntimeException("You are not authorized to view this file");
		}
		ApplicantReportInternalAttachmentsHandler.generateResponse(response, att);
	}
	
}
