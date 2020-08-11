package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.docflow.DocFlowException;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class DocumentDocFlowHandler extends NacidBaseRequestHandler {
    
    public DocumentDocFlowHandler(ServletContext servletContext) {
        super(servletContext);
    }


    public static String prepareUrlForDocFlow(int docId, int attType, int webApp, String applicationNumber, String returnUrl) {
        return "control/document_docflow/save?"
                + "docId=" + docId
                + "&amp;attType=" + attType
                + "&amp;applicationNumber=" + applicationNumber
                + "&amp;webApp=" + webApp
                + "&amp;returnUrl=" + Utils.escapeUrl(returnUrl);
    }

    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        int attId = DataConverter.parseInt(request.getParameter("docId"), 0);
        int attType = DataConverter.parseInt(request.getParameter("attType"), 0);
        int webApp = DataConverter.parseInt(request.getParameter("webApp"), 0);
        String applicationNumber = DataConverter.parseString(request.getParameter("applicationNumber"), null);
        String returnUrl = request.getParameter("returnUrl");

        if (webApp == 0) {
            throw Utils.logException(new Exception("No webApp id"));
        }

        SystemMessageWebModel sm = null;
        try {
            generateDocFlow(getNacidDataProvider(), applicationNumber, attType, attId, webApp, getLoggedUser(request, response).getUserId());
            sm = new SystemMessageWebModel("Генериран е деловоден номер на документа", SystemMessageWebModel.MESSAGE_TYPE_CORRECT);
        } catch (DocFlowException e) {
            sm = new SystemMessageWebModel("Проблем при генерирането на деловоден номер",
                    SystemMessageWebModel.MESSAGE_TYPE_ERROR);
            sm.addAttribute("Моля опитайте по- късно");
        }

        addSystemMessageToSession(request, WebKeys.SYSTEM_MESSAGE, sm);

        try {
            response.sendRedirect(request.getContextPath() + Utils.unEscapeUrl(returnUrl));
        }
        catch(IOException e) {
            throw Utils.logException(e);
        }
    }

    public static void generateDocFlow(NacidDataProvider nacidDataProvider, String applicationNumber, int attType, int attachmentId, int webApp, int userGenerated) throws DocFlowException {
        if (webApp == NacidDataProvider.APP_NACID_ID) {
            AttachmentDataProvider attDP = null;
            switch (attType) {
            case DocCategory.APPLICATION_ATTACHMENTS:
            case DocCategory.APPLICATION_ATTACHMENTS_DOCTORATE:
                attDP = nacidDataProvider.getApplicationAttachmentDataProvider();
                break;
            case DocCategory.DIPLOMA_EXAMINATIONS:
                attDP = nacidDataProvider.getDiplExamAttachmentDataProvider();
                break;
            case DocCategory.UNIVERSITY_EXAMINATIONS:
                attDP = nacidDataProvider.getUniExamAttachmentDataProvider();
                break;
            default:
                throw Utils.logException(new Exception("Invalid attachment type"));
            }

            Attachment att = attDP.getAttachment(attachmentId, false, false);

            if (att == null) {
                throw Utils.logException(new UnknownRecordException("record not found: " + attachmentId));
            }

            Date date = new Date();
            attDP.saveAttacment(att.getId(), att.getParentId(), att.getDocDescr(), att.getDocTypeId(), att.getCopyTypeId(), applicationNumber, date, null, null, null, 0, null, null, null, 0, userGenerated);

        } else if (webApp == NacidDataProvider.APP_NACID_REGPROF_ID) {
            RegprofApplicationAttachmentDataProvider attDP = null;
            switch (attType) {
            case DocCategory.REG_PROF_APPLICATION_ATTACHMENTS:
                attDP = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
                break;
            case DocCategory.REG_PROF_SUGGESTIONS:
                attDP = nacidDataProvider.getRegprofApplicationAttachmentDataProvider();
                break;
            case DocCategory.REG_PROF_DOCUMENT_EXAMINATIONS:
                attDP = nacidDataProvider.getDocExamAttachmentDataProvider();
                break;
            case DocCategory.REG_PROF_EXPERIENCE_EXAMINATIONS:
                attDP = nacidDataProvider.getProfessionExperienceAttachmentDataProvider();
                break;
            default:
                throw Utils.logException(new Exception("Invalid attachment type"));
            }

            RegprofApplicationAttachment attachment = attDP.getAttachment(attachmentId, false, false);
            if (attachment == null) {
                throw Utils.logException(new UnknownRecordException("record not found: " + attachmentId));
            }

            attDP.saveAttachment(attachment.getId(), attachment.getParentId(), attachment.getDocDescr(), attachment.getDocTypeId(), attachment.getCopyTypeId(), applicationNumber,
                    new Date(), null, null, null, 0, null, null, null, 0, userGenerated);

        }
        else {
            throw Utils.logException(new Exception("Invalid web application id"));
        }
    }
}
