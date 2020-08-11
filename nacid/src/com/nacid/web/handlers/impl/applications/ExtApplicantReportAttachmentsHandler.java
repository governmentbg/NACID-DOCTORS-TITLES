package com.nacid.web.handlers.impl.applications;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.impl.applications.ApplicantReportInternalAttachmentsHandler;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.external.applications.ExtApplicationAttachmentDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;


public class ExtApplicantReportAttachmentsHandler extends NacidBaseRequestHandler {

	public ExtApplicantReportAttachmentsHandler(ServletContext servletContext) {
		super(servletContext);
	}

	public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
		//idto na vytreshniq attachment document
		Integer attachmentId = DataConverter.parseInteger(request.getParameter("attachment_id"), null);
		
		ExtApplicationAttachmentDataProvider attDP = getNacidDataProvider().getExtApplicationAttachmentDataProvider();		
		Attachment att = attDP.getApplicationAttacment(attachmentId, true);
		if (att == null) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		ApplicantReportInternalAttachmentsHandler.generateResponse(response, att);
	}
	

}
