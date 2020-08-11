package com.ext.nacid.web.handlers.impl.applications;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.exceptions.UnknownRecordException;
import org.apache.commons.io.IOUtils;


public class ApplicantReportInternalAttachmentsHandler extends NacidExtBaseRequestHandler {
	public static final int ATTACHMENT_TYPE_APPLICANT_ATTACHMENTS = 1;
	public static final int ATTACHMENT_TYPE_UNI_EXAM_ATTACHMENTS = 2;
	public static final int ATTACHMENT_TYPE_DIPLOMA_EXAM_ATTACHMENTS = 3;
	public ApplicantReportInternalAttachmentsHandler(ServletContext servletContext) {
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

		//proverqva specifi4nata za applicant/expert authorization
		checkSpecificAuthorization(user, applicationId);

		
		//vytre6niq application
    	ApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getApplicationsDataProvider();
		Application application = applicationId == null ? null : applicationsDataProvider.getApplication(applicationId);
		if (application == null) {
			throw new UnknownRecordException("unknown application id..." + request.getParameter("id"));
		}
		//Proverqva obshtata authorizaciq
		Attachment att = checkBaseAuthorization(type, attachmentId, nacidDataProvider, application);
		
		String fileName = getOperationName(request);
        boolean scanned;
        if(fileName.equals(att.getFileName())) {
            scanned = false;
        }
        else if(fileName.equals(att.getScannedFileName())) {
            scanned = true;
        }
        else {
            throw Utils.logException(new Exception("Unknown fileName"));
        }
        
        AttachmentDataProvider attDP = null;
        switch(type) {
            case ATTACHMENT_TYPE_APPLICANT_ATTACHMENTS: 
                attDP = nacidDataProvider.getApplicationAttachmentDataProvider();
                break;
            case ATTACHMENT_TYPE_UNI_EXAM_ATTACHMENTS: 
                attDP = nacidDataProvider.getUniExamAttachmentDataProvider();
                break;
            case ATTACHMENT_TYPE_DIPLOMA_EXAM_ATTACHMENTS: 
                attDP = nacidDataProvider.getDiplExamAttachmentDataProvider();
                break;
        }
        
        att = attDP.getAttachment(attachmentId, !scanned, scanned);

		
		generateResponse(response, att, scanned);
	}
	
	public static void generateResponse(HttpServletResponse response, Attachment att) {
	    generateResponse(response, att, false);
	}
	
	public static void generateResponse(HttpServletResponse response, Attachment att, boolean scanned) {
	    InputStream is = null;
	    String fileName = null;
	    if(!scanned) {
	        response.setContentType(att.getContentType());
	        is = att.getContentStream();
	        fileName = att.getFileName();
	    }
	    else {
	        response.setContentType(att.getScannedContentType());
            is = att.getScannedContentStream();
            fileName = att.getScannedFileName();
	    }
		try {

			logger.debug("Content Length:" + is.available());
			response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)).replace("+", "%20"));

			IOUtils.copy(is, response.getOutputStream());
		} catch (Exception e) {
			throw Utils.logException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw Utils.logException(e);
				}
			}
		}
	}
	/**
	 *	Proverqva base authorizaciqta (koqto se otnasq i za ExpertReport i za ApplicantReport) -  t.e. dali zadadeniq attachmentId se otnasq za konkretniq application
		a specifi4nata authorizaciq za Expert i Applicant se izvyrshva v handleDefault-a na ApplicatnReportAttachementsHandler i ExpertReportAttachmentsHandler
		pod specifi4na se razbira dali konkretniq expert moje da razglejda konkretnoto zaqvlenie i dali konkretniq potrebitel moje da razglejda konkretnoto zaqvlenie
		Ako ima problem se hvyrlq RuntimeException, inache se vry6ta AttachmentBase object
	 */
	protected Attachment checkBaseAuthorization(int type, int attachmentId, NacidDataProvider nacidDataProvider, Application application) throws RuntimeException{
		Attachment att;
		//Shte pazi dali experta e autoriziran da gleda konkretniq file
		boolean unAuthorized = false;
		if (type == ATTACHMENT_TYPE_APPLICANT_ATTACHMENTS) {
			AttachmentDataProvider attDP = nacidDataProvider.getApplicationAttachmentDataProvider();
			Attachment att1 = attDP.getAttachment(attachmentId, false, false);
			//Proverqva se dali ima syotvetstvie mejdu id-to na attachmenta i application id-to
			if (att1 != null && att1.getParentId() != application.getId()) {
				unAuthorized = true;
			}
			att = att1;
		} else if (type == ATTACHMENT_TYPE_UNI_EXAM_ATTACHMENTS) {
			AttachmentDataProvider uniExamAttachmentDataProvider = nacidDataProvider.getUniExamAttachmentDataProvider();
			att = uniExamAttachmentDataProvider.getAttachment(attachmentId, false, false);
			List<Integer> attachmentsByApplication = uniExamAttachmentDataProvider.getAttachmentIdsByApplication(application.getId());
			//Proverqva se dali zadadeniq application ima uni exam attachment s konkretnoto id
			if (attachmentsByApplication == null || (att != null && !attachmentsByApplication.contains(att.getId()))) {
				unAuthorized = true;
			}
			
		} else if (type == ATTACHMENT_TYPE_DIPLOMA_EXAM_ATTACHMENTS) {
			AttachmentDataProvider diplExamAttachmentDataProvider = nacidDataProvider.getDiplExamAttachmentDataProvider();
			att = diplExamAttachmentDataProvider.getAttachment(attachmentId, false, false);
			List<Integer> attachmentsByApplication = diplExamAttachmentDataProvider.getAttachmentIdsByApplication(application.getId());
			//Proverqva se dali zadadeniq application ima uni exam attachment s konkretnoto id
			if (attachmentsByApplication == null || (att != null && !attachmentsByApplication.contains(att.getId()))) {
				unAuthorized = true;
			}
			
			
		} else {
			throw new RuntimeException("Attachment type not defined....");
		}
		
		if (unAuthorized) {
			throw new RuntimeException("You are not authorized to view this file.");
		}
		//Ako nqma application s tova ID
		if (att == null) {
			throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
		}
		return att;
	}
	
	protected void checkSpecificAuthorization(User user, int applicationId) throws RuntimeException{
		if (!UserAccessUtils.hasAccessToViewInternalApplicationWithId(applicationId, user, getNacidDataProvider())) {
			throw new RuntimeException("You are not authorized to view this application");
		}
	}

}
