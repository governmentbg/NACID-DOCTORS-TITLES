package com.nacid.web.handlers.impl.comission;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.handlers.NacidBaseRequestHandler;
import com.nacid.web.handlers.impl.applications.ApplicationAttachmentHandler;
import com.nacid.web.model.common.SystemMessageWebModel;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommissionCalendarPrintCertsHandler extends NacidBaseRequestHandler {

    ServletContext servletContext;
    public CommissionCalendarPrintCertsHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }
    private static Map<String, Integer> typeToDocumentTypeId = new HashMap<String, Integer>();
    static {
    	typeToDocumentTypeId.put("certs", DocumentType.DOC_TYPE_CERTIFICATE);
    	typeToDocumentTypeId.put("refused", DocumentType.DOC_TYPE_REFUSE);
    	typeToDocumentTypeId.put("postponed", DocumentType.DOC_TYPE_PISMO_OTLAGANE);
    }
    private static Map<String, Integer> typeToApplicationStatusId = new HashMap<String, Integer>();
    static {
    	typeToApplicationStatusId.put("certs", ApplicationStatus.APPLICATION_PRIZNATO_STATUS_CODE);
    	typeToApplicationStatusId.put("refused", ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE);
    	typeToApplicationStatusId.put("postponed", ApplicationStatus.APPLICATION_POSTPONED_STATUS_CODE);
    }
    
    @Override
    public void handleDefault(HttpServletRequest request, HttpServletResponse response) {
        
        int calendarId = DataConverter.parseInt(request.getParameter("calendarId"), -1);
        if(calendarId <= 0) {
            throw Utils.logException(new Exception("Unknown calendar id " + calendarId));
        }
        if(calendarId <= 0) {
            throw Utils.logException(new Exception("Unknown new app status id " + calendarId));
        }
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        CommissionCalendar calendar = nacidDataProvider.getCommissionCalendarDataProvider().getCommissionCalendar(calendarId);
        String type = request.getParameter("type");
        
        if (calendar.getSessionStatusId() != SessionStatus.SESSION_STATUS_PROVEDENO) {
        	String msg = " Статусът на комисията е различен от " + nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS, SessionStatus.SESSION_STATUS_PROVEDENO).getName();;
        	SystemMessageWebModel sysMsg = new SystemMessageWebModel(msg, SystemMessageWebModel.MESSAGE_TYPE_ERROR);
            addSystemMessageToSession(request, "printMsg" + type, sysMsg);
            sendRedirect(request, response, calendarId);
            return;
        }
        
        String selectedApplications = request.getParameter("selectedApplicationsPrint");
        int documentTypeId = typeToDocumentTypeId.get(type);
        int applicationStatusId = typeToApplicationStatusId.get(type);
        ArrayList<Integer> selIds = new ArrayList<Integer>();
        if(selectedApplications != null) {
            String[] ids = selectedApplications.split(";");
            for(String s : ids) {
                Integer id = DataConverter.parseInteger(s, null);
                if(id != null) {
                    selIds.add(id);
                }
            }
        }
        
        ApplicationsDataProvider appDP = nacidDataProvider.getApplicationsDataProvider();
        AttachmentDataProvider appAttDP = nacidDataProvider.getApplicationAttachmentDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();
        
        ArrayList<Integer> printIds = new ArrayList<Integer>();
        DocumentType docType = nomDP.getDocumentType(documentTypeId);
        FlatNomenclature applicationStatus = nomDP.getApplicationStatus(NumgeneratorDataProvider.NACID_SERIES_ID, applicationStatusId);
        
        
        List<ApplicationDetailsForReport> applications = new ArrayList<ApplicationDetailsForReport>();
        Map<Integer, ApplicationAttachmentHandler.GenerateAndSaveApplicationAttachmentResponse> savedAttachmentsResponse = new HashMap<>();
        for(Integer appId : selIds) {
            
            ApplicationDetailsForReport appl = appDP.getApplicationDetailsForReport(appId);
            List<Attachment> availableAttachmentsFromGivenType = appAttDP
                .getAttachmentsForParentByType(appId, docType.getId());
            boolean hasAttachment = availableAttachmentsFromGivenType != null && availableAttachmentsFromGivenType.size() > 0;
            Application a = appl.getApplication();
            if(a.getApplicationStatusId() == applicationStatusId
                    && !hasAttachment) {
                
                printIds.add(appId);
                ApplicationAttachmentHandler.GenerateAndSaveApplicationAttachmentResponse resp = ApplicationAttachmentHandler.generateAndSaveApplicationAttachment(nacidDataProvider, appl, docType, getLoggedUser(request, response).getUserId());
                savedAttachmentsResponse.put(appId, resp);
                applications.add(appl);
            }
            
        }
        
        if(printIds.size() <= 0 || calendar.getSessionStatusId() != SessionStatus.SESSION_STATUS_PROVEDENO) {
            
           SystemMessageWebModel sysMsg = new SystemMessageWebModel("Не са избрани заявления, които да са със статус \"" + applicationStatus.getName() + "\" и да нямат " + docType.getName(), SystemMessageWebModel.MESSAGE_TYPE_ERROR);
            addSystemMessageToSession(request, "printMsg" + type, sysMsg);
            sendRedirect(request, response, calendarId);
            return;
        }
        InputStream allCertsIs;
        if (DocumentType.RUDI_AND_RUDI_DOCTORATE_CERTIFICATE_TYPES.contains(documentTypeId) ) {
            List<TemplateGenerator.GenerateCertificateRequest> requests = applications.stream().map(r -> new TemplateGenerator.GenerateCertificateRequest(r, savedAttachmentsResponse.get(r.getApplication().getId()).getCertificateNumber(), savedAttachmentsResponse.get(r.getApplication().getId()).getUuid())).collect(Collectors.toList());
            allCertsIs = TemplateGenerator.generateCertificates(nacidDataProvider, documentTypeId, requests);
        } else {
            allCertsIs = TemplateGenerator.generateDocsFromTemplate(getNacidDataProvider(), applications.toArray(new ApplicationDetailsForReport[0]), docType);
        }

        
        response.setContentType("application/msword");
        writeInputStreamToResponse(response, allCertsIs);
    }
    private static void sendRedirect(HttpServletRequest request, HttpServletResponse response, int calendarId) {
    	try {
            response.sendRedirect( request.getSession().getServletContext().getAttribute("pathPrefix") + 
                    "/control/comission_calendar_process/edit/?calendar_id=" 
                    + calendarId);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
}
