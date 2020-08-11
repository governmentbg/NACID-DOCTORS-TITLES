package com.ext.nacid.regprof.web.handlers.impl.applications;

import com.ext.nacid.regprof.web.handlers.ExtRegprofUserAccessUtils;
import com.ext.nacid.web.handlers.UserAccessUtils;
import com.ext.nacid.web.handlers.impl.applications.ExtApplicationAttachmentHandler;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.exceptions.NotAuthorizedException;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.regprof.external.ExtRegprofApplicationAttachmentDataProvider;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExtRegprofApplicationAttachmentHandler extends ExtApplicationAttachmentHandler {

    public ExtRegprofApplicationAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
    }
    protected void checkApplicantActionAccess(int operationId, User user, int applicationId) {
        try {
            ExtRegprofUserAccessUtils.checkApplicantActionAccess(applicationId, user.getUserId(), operationId, getNacidDataProvider());
        } catch (NotAuthorizedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    protected int getDocumentCategoryId(Integer applicationId) {
        return DocCategory.REG_PROF_APPLICATION_ATTACHMENTS;
    }
    @Override
    protected Attachment getAttachment(int attachmentId, boolean loadContent) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationAttachmentDataProvider dp = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider();
        return dp.getApplicationAttacment(attachmentId, loadContent);
    }
    @Override
    protected List<Attachment> getAttachments(int applicationId) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationAttachmentDataProvider dp = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider();
        return dp.getAttachmentsForApplication(applicationId);
    }
    
    protected int saveAttachment(int id, int applicationId, String docDescr, int docTypeId, String contentType, String fileName, InputStream contentStream, int copyTypeId, int fileSize, int userCreated) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationAttachmentDataProvider dp = nacidDataProvider.getExtRegprofApplicationAttachmentDataProvider();
        return dp.saveApplicationAttacment(id, applicationId, docDescr, docTypeId, contentType, fileName, contentStream, copyTypeId, fileSize, userCreated);
    }
    
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int attId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attId <= 0) {
            throw new UnknownRecordException("Unknown attachment Id:" + attId);
        }

        ExtRegprofApplicationAttachmentDataProvider attDP = getNacidDataProvider().getExtRegprofApplicationAttachmentDataProvider();
        
        Attachment att = attDP.getApplicationAttacment(attId, false);
        if (att != null) {
            checkApplicantActionAccess(UserAccessUtils.USER_ACTION_CHANGE, getLoggedUser(request, response), att.getParentId());
        }

        attDP.deleteAttachment(attId);

        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
        // handleList(request, response);
        try {
            response.sendRedirect((String) request.getSession().getAttribute("backUrlApplAtt"));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
}
