package com.nacid.regprof.web.handlers.impl.applications;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ext.nacid.web.handlers.impl.applications.ApplicantReportInternalAttachmentsHandler;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.regprof.external.ExtRegprofApplicationAttachmentDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.web.exceptions.UnknownRecordException;

public class ExtRegprofApplicantReportAttachmentsHandler extends RegProfBaseRequestHandler {

    public ExtRegprofApplicantReportAttachmentsHandler(ServletContext servletContext) {
        super(servletContext);
    }
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        //idto na vytreshniq attachment document
        Integer attachmentId = DataConverter.parseInteger(request.getParameter("id"), null);
        
        ExtRegprofApplicationAttachmentDataProvider attDP = getNacidDataProvider().getExtRegprofApplicationAttachmentDataProvider();      
        Attachment att = attachmentId == null ? null : attDP.getApplicationAttacment(attachmentId, true);
        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        ApplicantReportInternalAttachmentsHandler.generateResponse(response, att);
    }
}
