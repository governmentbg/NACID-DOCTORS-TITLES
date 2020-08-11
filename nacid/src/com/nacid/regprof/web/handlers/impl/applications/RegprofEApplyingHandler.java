package com.nacid.regprof.web.handlers.impl.applications;

import com.ext.nacid.regprof.web.handlers.impl.applications.RegprofApplicantReportHandler;
import com.ext.nacid.regprof.web.model.applications.report.ExtPersonDocumentForReportWebModel;
import com.ext.nacid.web.model.applications.report.ExtPersonForReportWebModel;
import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.regprof.RegprofApplication;
import com.nacid.bl.applications.regprof.RegprofApplicationAttachmentDataProvider;
import com.nacid.bl.docflow.DocFlowException;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationDetailsImpl;
import com.nacid.bl.impl.regprof.external.applications.ExtRegprofApplicationImpl;
import com.nacid.bl.mail.MailDataProvider;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.regprof.external.ExtRegprofApplicationForList;
import com.nacid.bl.regprof.external.ExtRegprofApplicationsDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.Table;
import com.nacid.bl.table.TableRow;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.handlers.impl.applications.EApplyingHandler;
import com.nacid.web.model.common.SystemMessageWebModel;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.ext.nacid.regprof.web.handlers.impl.applications.ExtRegprofApplicationsHandler.getAppStatusName;


public class RegprofEApplyingHandler  extends EApplyingHandler {
    
    public RegprofEApplyingHandler(ServletContext servletContext) {
        super(servletContext);
    }
    @Override
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {
        Integer extApplId = DataConverter.parseInteger(request.getParameter("id"), null);
        Integer applicantId = savePersonIfNecessary(request, getNacidDataProvider(), "applicant");
        Integer representativeId = savePersonIfNecessary(request, getNacidDataProvider(), "representative");
        Integer personDocumentId = saveDocumentData(request, getNacidDataProvider(), applicantId);
        
        ExtRegprofApplicationsDataProvider extAppDP = getNacidDataProvider().getExtRegprofApplicationsDataProvider();
        
        try {
            RegprofApplication app = extAppDP.transferApplicationToIntDb(extApplId, getLoggedUser(request, response).getUserId(), applicantId, representativeId, personDocumentId);
            if(!StringUtils.isEmpty(extAppDP.getExtRegprofApplication(extApplId).getApplicationDetails().getAppXml())){
                int attachmentId = sendMessageToUser(request, response, app, extApplId);//TODO:SendMessageToUser trqbva da se preraboti za da mi vyrshi rabota i tuk!!!
            }
        } catch (DocFlowException e) {
            Utils.logException(e);
            request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel(MessagesBundle.getMessagesBundle().getValue("docflow_system_connection_error"),
                    SystemMessageWebModel.MESSAGE_TYPE_ERROR));
            handleEdit(request, response);
            return;
        }
        
        request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Заявлението е прехвърлено", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        request.setAttribute("SAVED", true);//toq parameter se setva, tyj kato sled tova se vika handleView, koeto vika handleEdit, koeto hvyrlq exception ako extApplication.getApplicationDetails().getRegprofApplicationId() != null, a to sled kato se prehvyrli syob6tenieto, 100% tova nqma da e null!
        removeCachedTables(request);
        handleView(request, response);
    }

    protected void resetTableData(HttpServletRequest request, HttpServletResponse response) {
        Table table = (Table) request.getSession().getAttribute(getTableName(request));
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider applicationsDataProvider = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        
        
        List<Integer> statuses = new ArrayList<Integer>();
        statuses.add(ExtRegprofApplicationImpl.STATUS_EDITABLE);
        statuses.add(ExtRegprofApplicationImpl.STATUS_NOT_EDITABLE);
        statuses.add(ExtRegprofApplicationImpl.STATUS_FINISHED);
        List<ExtRegprofApplicationForList> apps = applicationsDataProvider.getExtRegprofApplicationsByStatuses(statuses);
        
        if (apps != null) {
            for (ExtRegprofApplicationForList app : apps) {
                try {
                    
                    TableRow row = table.addRow(app.getId(), app.getDate(), app.getApplicantName(), app.isEsigned(), "Регулирани професии", getAppStatusName(app.getStatusId()), app.getStatusId(), app.isCommunicated());
                    row.setEditable(app.getStatusId() == ExtRegprofApplicationImpl.STATUS_NOT_EDITABLE);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }
    
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = DataConverter.parseInt(request.getParameter("id"), 0);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        ExtRegprofApplicationsDataProvider extAppDP = nacidDataProvider.getExtRegprofApplicationsDataProvider();
        ExtPersonDataProvider extPersonDataProvider = nacidDataProvider.getExtPersonDataProvider();
        ExtRegprofApplicationImpl extApplication = applicationId == 0 ? null : extAppDP.getExtRegprofApplication(applicationId);
        if (extApplication == null) {
            throw new RuntimeException("No application is set....");
        }
        if (extApplication.getApplicationDetails().getRegprofApplicationId() != null && request.getAttribute("SAVED") == null) {
            throw new RuntimeException("Application is already transferred");
        }
        ExtPerson applicant = extPersonDataProvider.getExtPerson(extApplication.getApplicationDetails().getApplicantId());
        //representative se popylva samo ako e razlichen ot applicant-a!!!!
        ExtPerson representative = Objects.equals(extApplication.getApplicationDetails().getApplicantId(), extApplication.getApplicationDetails().getRepresentativeId()) ? null : extPersonDataProvider.getExtPerson(extApplication.getApplicationDetails().getRepresentativeId());
        ExtPersonDocumentForReportWebModel extPersonDocument = extApplication.getApplicationDetails().getApplicantDocumentsId() == null ? null : new ExtPersonDocumentForReportWebModel(extPersonDataProvider.getExtPersonDocument(extApplication.getApplicationDetails().getApplicantDocumentsId()));
        request.setAttribute("applicant", new ExtPersonForReportWebModel(applicant));

        request.setAttribute("representative", representative == null ? null : new ExtPersonForReportWebModel(representative));
        request.setAttribute("documentData", extPersonDocument);
        
        
        initApplicantData(request, applicant, null, representative, null);

        RegprofApplicantReportHandler.generateExternalApplicantReport(getNacidDataProvider(), extApplication, request, "ext", applicant);

        new ExtRegprofApplicationCommentHandler(getServletContext()).handleList(request, response);


    }
    /**
     * izprashta message do user-a, vyvel external Application-a s informaciq 4e zaqvlenieto mu e prehvyrleno vyv vytre6nata baza i e polu4ilo dadeniq delovoden nomer.
     * @param request
     * @param response
     * @param extApplicationId
     * @return attachmentId-to na izprateniq mail ( mail-a se dobavq kato attached document kym dadenoto zaqvlenie)
     */
    private int sendMessageToUser(HttpServletRequest request, HttpServletResponse response, RegprofApplication app, int extApplicationId) {
        ExtRegprofApplicationsDataProvider extApplicationsDataProvider = getNacidDataProvider().getExtRegprofApplicationsDataProvider();
        ExtRegprofApplicationDetailsImpl extApplicationDetails = extApplicationsDataProvider.getApplicationDetails(extApplicationId);
        
        
        UtilsDataProvider utilsDataProvider = getNacidDataProvider().getUtilsDataProvider();
        
        String msgBody = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_TRANSFERRED_MESSAGE);
        msgBody = MessageFormat.format(msgBody, DataConverter.formatDateTime(extApplicationDetails.getDateSubmitted(), false), app.getDocFlowNumber());
        String msgSubject = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.APPLICATION_TRANSFERRED_SUBJECT);
        
        MailDataProvider mailDataProvider = getNacidDataProvider().getMailDataProvider();
        String sender = utilsDataProvider.getCommonVariableValue(UtilsDataProvider.REGPROF_MAIL_SENDER);
        Integer emailId = mailDataProvider.sendMessage(sender, sender, app.getApplicant().getFullName(), app.getEmail(), msgSubject, msgBody);
        extApplicationsDataProvider.saveApplicationComment(extApplicationId, "Subject:"  + msgSubject + "\nBody:" + msgBody, true, emailId, true, getLoggedUser(request, response).getUserId());
        
        RegprofApplicationAttachmentDataProvider attDP = getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
        StringBuilder sb = new StringBuilder();
        sb.append("From:" + sender + "\n\r");
        sb.append("To:" + app.getEmail() + "\n\r");
        sb.append("Subject:" + msgSubject + "\n\r");
        sb.append("Body:" + msgBody);
        byte[] data;
        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw Utils.logException(e);
        }
        InputStream is = new ByteArrayInputStream(data);
        return attDP.saveAttachment(0, app.getApplicationDetails().getId(), msgBody, 
                DocumentType.DOC_TYPE_MESSAGE_BY_EMAIL_OUT, 
                null, 
                null, null, 
                "text/plain", "mail_out_" + DataConverter.formatDateTime(new Date(), false) + ".txt", 
                is, data.length, 
                null, null, null, 0, getLoggedUser(request, response).getUserId());
    }
    
}
