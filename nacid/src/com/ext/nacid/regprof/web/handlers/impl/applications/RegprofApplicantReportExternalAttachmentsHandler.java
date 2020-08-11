package com.ext.nacid.regprof.web.handlers.impl.applications;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.ext.nacid.web.handlers.impl.applications.ApplicantReportInternalAttachmentsHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.external.applications.ExtApplication;
import com.nacid.bl.external.applications.ExtApplicationAttachmentDataProvider;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.regprof.external.ExtRegprofApplicationAttachmentDataProvider;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.exceptions.UnknownRecordException;

public class RegprofApplicantReportExternalAttachmentsHandler extends NacidExtBaseRequestHandler {

    public RegprofApplicantReportExternalAttachmentsHandler( ServletContext servletContext) {
        super(servletContext);
        
    }
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        //id-to na vytre6niq application
        Integer applicationId = DataConverter.parseInteger(request.getParameter("application_id"), null);
        //idto na vytreshniq attachment document
        Integer attachmentId = DataConverter.parseInteger(request.getParameter("id"), null);
        
        if (applicationId == null || attachmentId == null ) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        
        ExtRegprofApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationDetailsImpl application = applicationId == null ? null : applicationsDataProvider.getApplicationDetails(applicationId);
        if (application == null) {
            throw new UnknownRecordException("unknown application id..." + request.getParameter("application_id"));
        }
        try {
            ExtRegprofUserAccessUtils.checkApplicantActionAccess(ExtRegprofUserAccessUtils.USER_ACTION_VIEW, getExtPerson(request, response), application);
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e);
        }
        ExtRegprofApplicationAttachmentDataProvider extApplicationAttachmentDataProvider = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider();
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
