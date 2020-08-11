package com.ext.nacid.web.handlers;

import javax.servlet.ServletContext;

import com.ext.nacid.web.handlers.impl.applications.ApplicantReportInternalAttachmentsHandler;
//import com.nacid.bl.external.users.ExtUser;
import com.nacid.bl.users.User;

public class PublicReportAttHandler extends ApplicantReportInternalAttachmentsHandler {

    public PublicReportAttHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    protected void checkSpecificAuthorization(User user, int applicationId) throws RuntimeException {
        
    }
    
    
}
