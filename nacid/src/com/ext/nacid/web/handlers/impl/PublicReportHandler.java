package com.ext.nacid.web.handlers.impl;

import com.ext.nacid.web.handlers.NacidExtBaseRequestHandler;
import com.ext.nacid.web.handlers.impl.applications.ExpertReportHandler;
import com.nacid.bl.applications.Application;
import com.nacid.bl.applications.ApplicationsDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.AttachmentDataProvider;
import com.nacid.bl.docflow.DocFlowNumber;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.DataConverter;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.UserOperationsUtils;
import com.nacid.web.model.applications.report.base.AttachmentForReportBaseWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PublicReportHandler extends NacidExtBaseRequestHandler {

    
    public PublicReportHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String operationName = getOperationName(request);
        int operationId = UserOperationsUtils.getOperationId(operationName);

        if (operationId == UserOperationsUtils.OPERATION_LEVEL_NEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Добавяне на");
            handleNew(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_EDIT) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleEdit(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_VIEW) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Преглед на");
            handleView(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_SAVE) {
            request.setAttribute(WebKeys.OPERATION_STRING_FOR_SCREENS, "Промяна на");
            handleSave(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_LIST) {
            handleList(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_DELETE) {
            handleDelete(request, response);
        } else if (operationId == UserOperationsUtils.OPERATION_LEVEL_PRINT) {
            handlePrint(request, response);
        } else {
            handleDefault(request, response);
        }

    }
    
    @Override
    public void handleView(HttpServletRequest request, HttpServletResponse response) {
        
        String personalId = request.getParameter("personalId");
        String appNum = request.getParameter("appNum");
        String certNum = request.getParameter("certnum");
        Integer appId = null;
        boolean searchByAppNum = !StringUtils.isEmpty(appNum);
        
        ApplicationsDataProvider appDP = getNacidDataProvider().getApplicationsDataProvider();
        if (searchByAppNum) {
            DocFlowNumber df = new DocFlowNumber(appNum, true);
            if (df.isValid()) {
                appId = appDP.getApplicationIdForExtReport(df.getDocflowId(), df.getDocflowDate(), personalId);
            }
        } else { //search by certNum
        	appId = appDP.getApplicationIdForExtReport(certNum, personalId);
        }
        
        if(appId != null) {
            ExpertReportHandler.prepareExpertReport(request, response, getNacidDataProvider(), appId);
            
            
            //Generira nanovo ApplicationAttachments kato slaga samo udostoverenieto, ako ima takova!
            AttachmentDataProvider attachmentDataProvider = getNacidDataProvider().getApplicationAttachmentDataProvider();
            List<Attachment> attachments = attachmentDataProvider.getAttachmentsForParentByType(appId, DocumentType.DOC_TYPE_CERTIFICATE);
            if (attachments != null) {
            	List<AttachmentForReportBaseWebModel> attachmentsWebModel = new ArrayList<AttachmentForReportBaseWebModel>();
    			for (Attachment a:attachments) {
    				attachmentsWebModel.add(new AttachmentForReportBaseWebModel(a));
    			}
    			request.setAttribute(WebKeys.APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, attachmentsWebModel);
            }
            
            
        }
        else {
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel(
                    "Не е намерено заявление", SystemMessageWebModel.MESSAGE_TYPE_ERROR));
        }
        setNextScreen(request, "public_report");
    }
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("application_id"), 0);
        //int registerType = DataConverter.parseInt(request.getParameter("type"), 0);
        
        
        Application app = getNacidDataProvider().getApplicationsDataProvider()
                .getApplication(applicationId);
        
        int[] docTypes = PublicRegisterHandler.registerType2DocTypes.get(PublicRegisterHandler.REGISTER_TYPE_IZDADENI);
        if(app == null || docTypes == null) {
            throw new UnknownRecordException("No such application"); 
        }
        
        Attachment a = getNacidDataProvider().getApplicationAttachmentDataProvider()
            .loadattachmentForPublicRegister(applicationId, docTypes);
        
        if(a == null) {
            throw new UnknownRecordException("Документът не е наличен");
        }
        
        response.setContentType(a.getScannedContentType());
        
        InputStream is = a.getScannedContentStream();
        try {
            ServletOutputStream sos = response.getOutputStream();

            int read = 0;
            byte[] buf = new byte[1024];

            while ((read = is.read(buf)) > 0) {
                sos.write(buf, 0, read);
            }

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
    
}
