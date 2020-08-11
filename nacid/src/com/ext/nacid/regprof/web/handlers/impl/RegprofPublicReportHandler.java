package com.ext.nacid.regprof.web.handlers.impl;

import com.ext.nacid.regprof.web.handlers.RegProfExtBaseRequestHandler;
import com.ext.nacid.regprof.web.handlers.impl.applications.RegprofApplicantReportHandler;
import com.ext.nacid.web.handlers.impl.PublicRegisterHandler;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachment;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplicationDataProvider;
import com.nacid.bl.docflow.DocFlowNumber;
import com.nacid.bl.impl.Utils;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
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

//RayaWritten------------------------------------------
public class RegprofPublicReportHandler extends RegProfExtBaseRequestHandler{

    public RegprofPublicReportHandler(ServletContext servletContext) {
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

        RegprofApplicationDataProvider appDP = getNacidDataProvider().getRegprofApplicationDataProvider();
        if (searchByAppNum) {
            DocFlowNumber df = new DocFlowNumber(appNum, true);

            if(df.isValid()) {
                appId = appDP.getRegprofApplicationIdForExtReport(df.getDocflowId(), df.getDocflowDate(), personalId);
            }   
        } else { //search by certNum
            appId = appDP.getRegprofApplicationIdForExtReport(certNum, personalId);
        }

        if(appId != null) {
            RegprofApplication app = appDP.getRegprofApplication(appId);
            RegprofApplicantReportHandler.generateInternalApplicantReport(getNacidDataProvider(), app, request, null);

            //Generira nanovo ApplicationAttachments kato slaga samo udostoverenieto, ako ima takova!
            RegprofApplicationAttachmentDataProvider attachmentDataProvider = getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
            RegprofCertificateNumberToAttachedDocRecord certToNum = appDP.getCertificateNumber(appId, 0);
            List<AttachmentForReportBaseWebModel> attachmentsWebModel = new ArrayList<AttachmentForReportBaseWebModel>();
            if(certToNum != null && certToNum.getAttachedDocId() != null){
                RegprofApplicationAttachment attachment = attachmentDataProvider.getAttachment(certToNum.getAttachedDocId(), false, false);                
                if (attachment != null) {                    
                    attachmentsWebModel.add(new AttachmentForReportBaseWebModel(attachment));
                }
            } 
            request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_FOR_REPORT_WEB_MODEL, attachmentsWebModel);
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


        RegprofApplication app = getNacidDataProvider().getRegprofApplicationDataProvider()
        .getRegprofApplication(applicationId);

        int[] docTypes = RegprofPublicRegisterHandler.registerType2DocTypes.get(PublicRegisterHandler.REGISTER_TYPE_IZDADENI);
        if(app == null || docTypes == null) {
            throw new UnknownRecordException("No such application"); 
        }

        RegprofApplicationAttachment a = getNacidDataProvider().getRegprofApplicationAttachmentDataProvider()
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
//------------------------------------------------------
