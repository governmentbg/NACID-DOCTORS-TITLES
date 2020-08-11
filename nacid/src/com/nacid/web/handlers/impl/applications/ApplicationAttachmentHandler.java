package com.nacid.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.events.Event;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.table.*;
import com.nacid.bl.utils.UtilsDataProvider;
import com.nacid.data.DataConverter;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.model.applications.ApplicationAttachmentWebModel;
import com.nacid.web.model.applications.ApplicationWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.nacid.bl.nomenclatures.DocumentType.*;

public class ApplicationAttachmentHandler extends BaseAttachmentHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_DOCFLOW_NUM = "Деловоден номер";
    private static final String COLUMN_NAME_TYPE = "Тип документ";
    private static final String COLUMN_NAME_DESC = "Описание";
    private static final String COLUMN_NAME_COPY = "Форма";
    private static final String COLUMN_NAME_FILE_NAME = "Име";
    private static final String COLUMN_NAME_PREVIEW = "Preview";

    private static final String APPLICATION_PARAM = "applID";
    private static final String EDIT_SCREEN = "application_attachment_edit";

    

    public ApplicationAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        int applicationId = Integer.parseInt(request.getParameter(APPLICATION_PARAM));
        List<Attachment> factualErrors = getAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_FACTUAL_ERROR_APPLY);
        List<Attachment> duplicateApply = getAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_DUPLICATE_APPLY);
        List<Integer> certIds = new ArrayList<Integer>();
        if (factualErrors != null && factualErrors.size() > 0) {
        	certIds.add(DOC_TYPE_CERTIFICATE);
        }
        if (duplicateApply != null && duplicateApply.size() > 0) {
        	certIds.add(DocumentType.DOC_TYPE_CERTIFICATE_DUPLICATE);
        }
        request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(applicationId, certIds));
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);

        ComboBoxUtils.generateNomenclaturesComboBox(null, NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, getNacidDataProvider()
                .getNomenclaturesDataProvider(), true, request, "copyTypeCombo", null, true);

        generateDocTypesComboBox(applicationId, null, request);
        
        generateEventTypeCombo(null, request);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {

        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        AttachmentDataProvider attDP = getAttachmentDataProvider();

        Attachment att = attDP.getAttachment(attachmentId, false, false);

        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }

        String dfUrl = getDocFlowUrl(getNacidDataProvider(), att.getParentId(), attachmentId, 
                getEditUrl(attachmentId));
        request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(att.getId(), att.getParentId(), att
                .getDocDescr(), att.getFileName(), att.getScannedFileName(), 
                att.getDocflowNum(),
                att.getDocTypeId(),
                dfUrl));

        ComboBoxUtils.generateNomenclaturesComboBox(att.getCopyTypeId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, nacidDataProvider
                .getNomenclaturesDataProvider(), true, request, "copyTypeCombo", null, true);

        generateDocTypesComboBox(att.getParentId(), att.getDocTypeId(), request);
       
        
        Event ev = Utils.getListFirstElement(getNacidDataProvider().getEventDataProvider()
            .getEventsForDocument(attachmentId, att.getDocType().getId()));
        generateEventStatusCombo(ev != null ? ev.getEventStatus() : null, request);
        generateEventTypeCombo(ev != null ? ev.getEventTypeId() : null, request);
        
        addEventDatesToRequest(request, ev, getNacidDataProvider());
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {

        
        //int applicationId = DataConverter.parseInt(request.getParameter("id"), -1);
        ApplicationWebModel appWM = (ApplicationWebModel)request.getAttribute(WebKeys.APPLICATION_WEB_MODEL);
        int applicationId = Integer.parseInt(appWM.getId());
        String operation = getOperationName(request);
        if(operation.equals("save")) {
            operation = "edit";
        }
        String group = getGroupName(request);
        String query = "activeForm=3&id=" + applicationId;
        request.getSession().setAttribute("backUrlApplAtt", 
                request.getContextPath()
                + "/control" 
                + "/" + group 
                + "/" + operation
                + "?" + query);

        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_APPLICATION_ATTCH);

        // boolean reloadTable =
        // RequestParametersUtils.getParameterReloadTable(request);
        boolean reloadTable = true;
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DOCFLOW_NUM, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DESC, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_COPY, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_FILE_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PREVIEW, CellValueDef.CELL_VALUE_TYPE_STRING);

            
            session.setAttribute(WebKeys.TABLE_APPLICATION_ATTCH, table);
            resetTableData(request, applicationId);

        }

        // TableState settings
        TableState tableState = (TableState) session.getAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table);
            session.setAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel(null);
        webmodel.setGroupName("application_attachment");
        webmodel.insertTableData(table, tableState);
        request.setAttribute(WebKeys.TABLE_WEB_MODEL, webmodel);
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, APPLICATION_PARAM, applicationId + "");
        webmodel.setViewOpenInNewWindow(true);
        
        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session.getAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();

            session.setAttribute(WebKeys.TABLE_APPLICATION_ATTCH + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    @SuppressWarnings("rawtypes")
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        AttachmentDataProvider attDP = getAttachmentDataProvider();
        
        // create file upload factory and upload servlet
        DiskFileItemFactory factory = new DiskFileItemFactory();

        FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(getServletContext());
        factory.setFileCleaningTracker(pTracker);
        ServletFileUpload upload = new ServletFileUpload(factory);

        // set file upload progress listener
        FileUploadListener listener = new FileUploadListener();

        session.setAttribute(WebKeys.FILE_UPLOAD_LISTENER, listener);

        // upload servlet allows to set upload listener
        upload.setProgressListener(listener);

        try {
            List items = upload.parseRequest(request);

            SaveAttachmentRequest rq = new SaveAttachmentRequest(items, request, null);
            Attachment attachment = saveFile(request, response, rq);

            int applicationId = rq.getApplicationId();

            if (attachment == null) {
                request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(0, rq.getApplicationId(), rq.getDocDescr(), "", null, rq.getDocflowNum(), rq.getDocTypeId(), rq.getDocflowUrl()));
            } else {
                resetTableData(request, applicationId );

                saveReminder(rq.getEventType(), rq.getEventStatus(), attachment, applicationId);

                String docflowUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, attachment.getId(), getEditUrl(attachment.getId()));

                request.setAttribute(WebKeys.APPLICATION_ATTCH_WEB_MODEL, new ApplicationAttachmentWebModel(attachment.getId(), applicationId, attachment.getDocDescr(), attachment.getFileName(), attachment.getScannedFileName(), attachment.getDocflowNum(), attachment.getDocTypeId(), docflowUrl));
            }
			

            ComboBoxUtils.generateNomenclaturesComboBox(rq.getCopyTypeId(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, getNacidDataProvider().getNomenclaturesDataProvider(), true, request, "copyTypeCombo", null, true);
            generateDocTypesComboBox(applicationId, rq.getDocTypeId(), request);
            
            
            
            Event ev = null;
            if(attachment != null) {
                ev = Utils.getListFirstElement(getNacidDataProvider().getEventDataProvider()
                        .getEventsForDocument(attachment.getId(), attachment.getDocType().getId()));
            }
            generateEventStatusCombo(ev != null ? ev.getEventStatus() : null, request);
            generateEventTypeCombo(ev != null ? ev.getEventTypeId() : null, request);
                
            addEventDatesToRequest(request, ev, getNacidDataProvider());

            request.setAttribute(WebKeys.APPLICATION_ID, applicationId);
            
        } catch (Exception e) {
            throw Utils.logException(this, e);
        } finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }

        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
        
        

    }

    /**
     *@return attachment object ako e uspqlo da zapishe attachment-a v bazata, inache null. Ako ne moje da zapishe zapis v bazata slaga i error SystemMessageWebModel kato request attribute. Ako uspee da go zapishe, slaga correct SystemMessageWebModel
     * @throws IOException
     */
    protected Attachment saveFile(HttpServletRequest request, HttpServletResponse response, SaveAttachmentRequest rq) throws IOException {
        ApplicationsDataProvider applicationsDataProvider = getNacidDataProvider().getApplicationsDataProvider();
        AttachmentDataProvider attDP = getNacidDataProvider().getApplicationAttachmentDataProvider();
        boolean hasError = false;
        String errMsg = null;
        int applicationId = rq.getApplicationId();
        Integer docTypeId = rq.getDocTypeId();
        String certificateNumber = rq.getCertificateNumber();
        UUID certificateUuid = null;
        int id = rq.getId();
        boolean generate = rq.isGenerate();
        String contentType = rq.getContentType();
        String fileName = rq.getFileName();
        InputStream is = rq.getIs();
        int fileSize = rq.getFileSize();
        int copyTypeId = rq.getCopyTypeId();
        String docDescr = rq.getDocDescr();
        String scannedFileName = rq.getScannedFileName();
        InputStream scannedIs = rq.getScannedIs();
        String scannedContentType = rq.getScannedContentType();
        int scannedFileSize = rq.getScannedFileSize();

        if (docTypeId != null && docTypeId == DOC_TYPE_CERTIFICATE) {
            List<Attachment> applCerts = getAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DOC_TYPE_CERTIFICATE);
            Set<Integer> applCertIds = new HashSet<Integer>();
            if (applCerts != null) {
                for (Attachment a:applCerts) {
                    applCertIds.add(a.getId());
                }
            }
            //Ako ima ve4e vyvedeno udostoverenie i NE se editva zapisa na udostoverenieto - togava hasError stava = true
            //2011.01.18 - priemam 4e ako se vyvejda vtoro zaqvlenie, to 100% certificateNumber e popylneno, t.e. ne e presko4ena javaScript proverkata za setvane na toq parametyr
            if (certificateNumber == null && applCertIds.size() > 0 && (id == 0 || !applCertIds.contains(id))) {
                hasError = true;
                String name = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentType(DOC_TYPE_CERTIFICATE).getName();
                errMsg = "Заявлението вече има издадено " + name;
            }
        }
        Application app = applicationsDataProvider.getApplication(applicationId);
        Attachment attachment = id == 0 ? null : attDP.getAttachment(id, false, false);
        if (!hasError) {
            if (id != 0 && attachment == null) {
                throw new UnknownRecordException("Unknown attachment ID:" + id);
            }
            if (generate) {
                NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
                DocumentType docType = nomDP.getDocumentType(docTypeId);
                contentType = null;
                fileName = null;
                is = null;
                fileSize = 0;
                if (docType != null && !docType.isIncoming() && docType.getDocumentTemplate() != null
                        && docType.getDocumentTemplate().length() > 0) {

                    int appStstus = app != null ? app.getApplicationStatusId() : 0;
                    if (!attDP.hasAccessToGenerateDocument(appStstus, docType.getId())) {
                        errMsg = "Заявлението не е с подходящ статус за генериране на този тип документ";
                        hasError = true;
                    } else {
                        errMsg = checkApplicationCertificateGenerationPossibility(applicationId, docType.getId(), getNacidDataProvider());
                        hasError = (errMsg != null);
                    }
                    if (!hasError) {
                        contentType = "application/msword";
                        fileName = app.getApplicationNumber() + "_" + docType.getDocumentTemplate() + "_" + DataConverter.formatDate(new Date())
                                + ".doc";
                        fileName = fileName.replace("/", "_");

                        //Ako trqbva da se generira udostoverenie, to pyrvo se proverqva dali moje da se generira udosoverenie za tova zaqvlenie. Ako ne moje se hvyrlq exception
                        //sydyrjasht Message, koito trqbva da se pokaje na user-a
                        try {
                            if (id == 0 && RUDI_AND_RUDI_DOCTORATE_CERTIFICATE_TYPES.contains(docTypeId) && !hasError && certificateNumber == null) {
                                certificateNumber = applicationsDataProvider.generateCertificateNumber(applicationId);
                            }
                            //Ako se generira certificate ili dublikat na certificate, to se vika specialniq method za generirane na certificate, t.k. za nego v nqkoi slu4ai trqbva da se podade certificateNumber...
                            if (DocumentType.RUDI_AND_RUDI_DOCTORATE_CERTIFICATE_TYPES.contains(docTypeId)) {
                                certificateUuid = UUID.randomUUID();
                                is = TemplateGenerator.generateCertificate(getNacidDataProvider(), docTypeId, new TemplateGenerator.GenerateCertificateRequest(getNacidDataProvider().getApplicationsDataProvider().getApplicationDetailsForReport(applicationId), certificateNumber, certificateUuid));
                            } else {
                                is = TemplateGenerator.generateDocFromTemplate(getNacidDataProvider(), applicationId, docType);
                            }
                            fileSize = is.available();
                        } catch (CertificateNumberGenerationException e) {
                            hasError = true;
                            errMsg = e.getMessage();
                        }
                    }
                } else if (!(docTypeId == DocumentType.DOC_TYPE_MESSAGE_BY_EMAIL_OUT && copyTypeId == CopyType.ELECTRONIC_FORM)){
                    errMsg = MessagesBundle.getMessagesBundle().getValue("autogeneratedDocsConditions");
                    hasError = true;
                }
            }

            if (docTypeId == null && attachment != null)
                docTypeId = attachment.getDocTypeId();

            if (docTypeId == DocumentType.DOC_TYPE_MESSAGE_BY_EMAIL_OUT && copyTypeId == CopyType.ELECTRONIC_FORM) {

                UtilsDataProvider uDP = getNacidDataProvider().getUtilsDataProvider();
                String sender = uDP.getCommonVariableValue(UtilsDataProvider.MAIL_SENDER);
                String subject = uDP.getCommonVariableValue(UtilsDataProvider.MAIL_NOTIFICATION_SUBJECT);
                String recipient = app.getEmail();

                if (recipient == null) {
                    errMsg = "Няма посочена електронна поща на заявителя";
                    hasError = true;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("From:" + sender + "\r\n");
                    sb.append("To:" + recipient + "\r\n");
                    sb.append("Subject:" + subject + "\r\n");
                    sb.append("Body:" + docDescr);
                    byte[] btext = sb.toString().getBytes("UTF-8");
                    is = new ByteArrayInputStream(btext);
                    fileSize = btext.length;
                    contentType = "text/plain";
                    fileName = "email_out_" + DataConverter.formatDateTime(new Date(), false) + ".txt";

                    getNacidDataProvider().getMailDataProvider().sendMessage(null, sender, null, recipient, subject, docDescr);
                }

            }
        }

        if (id == 0 && fileSize <= 0) {
            hasError = true;
            if(errMsg == null) {
                errMsg = "Не е посочен файл";
            }
        }
        if (hasError) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
            request.setAttribute("attachmentStatusMessage", webmodel);
        }
        else if(!areFilenamesAllowed(fileName, scannedFileName, id, getAttachmentDataProvider())) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel("За този документ вече е прикачен файл с това име");
            request.setAttribute("attachmentStatusMessage", webmodel);
        } else {
            int newId = attDP.saveAttacment(id, applicationId, docDescr,
                    docTypeId, copyTypeId,
                    attachment != null ? attachment.getDocflowId() : null,
                    attachment != null ? attachment.getDocflowDate() : null,
                    contentType,
                    fileName, is, fileSize,
                    scannedContentType, scannedFileName,
                    scannedIs, scannedFileSize, certificateNumber, certificateUuid, getLoggedUser(request, response).getUserId());

            request.setAttribute("attachmentStatusMessage", new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));


            return attDP.getAttachment(newId, false, false);
        }
        return null;
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int attId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attId <= 0) {
            throw new UnknownRecordException("Unknown attachment Id:" + attId);
        }

        AttachmentDataProvider attDP = getAttachmentDataProvider();
        Attachment att = attDP.getAttachment(attId, false, false);
        attDP.deleteAttachment(attId);
        request.setAttribute(WebKeys.APPLICATION_ID, att.getParentId());
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
        // handleList(request, response);
        try {
            response.sendRedirect((String) request.getSession().getAttribute("backUrlApplAtt"));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    private void resetTableData(HttpServletRequest request, int applicationId) {
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        AttachmentDataProvider attDP = getAttachmentDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();

        List<Attachment> attachments = attDP.getAttachmentsForParent(applicationId);

        if (attachments != null) {
            for (Attachment att : attachments) {

                String copyType = "";
                FlatNomenclature ct = nomDP.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE, att.getCopyTypeId());
                if (ct != null) {
                    copyType = ct.getName();
                }

                String docType = "";
                DocumentType dt = nomDP.getDocumentType(att.getDocTypeId());
                if (dt != null) {
                    docType = dt.getLongName();
                }
                
                String fileName = att.getScannedFileName() == null ? att.getFileName() : att.getScannedFileName();
                
                try {

                    table.addRow(att.getId(), att.getDocflowNum(), docType, att.getDocDescr(), copyType, fileName, "")
                        .setDeletable(StringUtils.isEmpty(att.getDocflowNum()));
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }    
    
    protected String getEditUrl(int id) {
        return "/control/application_attachment/edit?id=" + id;
    }

    @Override
    protected AttachmentDataProvider getAttachmentDataProvider() {
        return getNacidDataProvider().getApplicationAttachmentDataProvider();
    }

    @Override
    protected int getDocTypeCategory(Integer applicationId) {
        int applicationType = getNacidDataProvider().getApplicationsDataProvider().getApplicationType(applicationId);
        return applicationType == ApplicationType.DOCTORATE_APPLICATION_TYPE ? DocCategory.APPLICATION_ATTACHMENTS_DOCTORATE : DocCategory.APPLICATION_ATTACHMENTS;
    }
    @Getter
    @AllArgsConstructor
    public static class GenerateAndSaveApplicationAttachmentResponse {
        private String certificateNumber;
        private UUID uuid;
        private int attId;
    }
    /**
     * generira applicationAttachment za dadeniq documentType i Application, kato zapisva zapisa v bazata i vry6ta ID-to na zapisaniq zapis!
     * zasega se polzva v Commission*Handlers
     * @param nacidDataProvider
     * @param application
     * @param documentType
     * @return idto na zapisnaiq v bazata zapis
     */
    public static GenerateAndSaveApplicationAttachmentResponse generateAndSaveApplicationAttachment(NacidDataProvider nacidDataProvider, ApplicationDetailsForReport application, DocumentType documentType, int userCreated) {
    	InputStream is;
    	String certificateNumber = null;
    	UUID uuid = null;
    	if (DocumentType.RUDI_AND_RUDI_DOCTORATE_CERTIFICATE_TYPES.contains(documentType.getId())) {
    	    certificateNumber = nacidDataProvider.getApplicationsDataProvider().generateCertificateNumber(application.getApplication().getId());
    	    uuid = UUID.randomUUID();
    	    is = TemplateGenerator.generateCertificate(nacidDataProvider, documentType.getId(), new TemplateGenerator.GenerateCertificateRequest(application, certificateNumber, uuid));
        } else {
            is = TemplateGenerator.generateDocFromTemplate(nacidDataProvider, application, documentType);
        }

    	String fileName = application.getApplicationNumber() + "_"
            + documentType.getDocumentTemplate()  + "_"
            + DataConverter.formatDate(new Date()) + ".doc";
        
        try {
            int attId = nacidDataProvider.getApplicationAttachmentDataProvider().saveAttacment(0, application.getApplication().getId(), null,
                    documentType.getId(), null,
                    null, null,
                    "application/msword", fileName,
                    is, is.available(),
                    null, null,
                    null, 0, certificateNumber, uuid, userCreated);
            return new GenerateAndSaveApplicationAttachmentResponse(certificateNumber, uuid, attId);
            //return is;
        } catch (IOException e) {
        	throw Utils.logException(e);
		}
    }

    /**
     * proverqva dali moje da se generira udostoverenie/dublikat/udostoverenie poradi qvna fakticheska greshka/, ako docTypeId e edin ot tezi statusi!
     * @param applicationId
     * @param docTypeId
     * @param nacidDataProvider
     * @return
     */

    public static String checkApplicationCertificateGenerationPossibility(int applicationId, int docTypeId, NacidDataProvider nacidDataProvider) {
        AttachmentDataProvider attachmentDataProvider = nacidDataProvider.getApplicationAttachmentDataProvider();
        NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
        if (docTypeId == DOC_TYPE_CERTIFICATE || docTypeId == DOC_TYPE_DOCTORATE_CERTIFICATE) {
            List<Attachment> applCerts = attachmentDataProvider.getAttachmentsForParentByType(applicationId, docTypeId);

            if (applCerts != null && applCerts.size() > 0) {
                String name = nomenclaturesDataProvider.getDocumentType(docTypeId).getName();
                return "Заявлението вече има издадено " + name;
            }

        } else if (docTypeId == DocumentType.DOC_TYPE_CERTIFICATE_DUPLICATE || docTypeId == DOC_TYPE_DOCTORATE_CERTIFICATE_DUPLICATE) {
            List<Attachment> duplicateApply = attachmentDataProvider.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_DUPLICATE_APPLY);
            //04.11.2011 - казаха че при издаване на дубликат на удостоверение, трябва да се махне проверката дали заявлението има издадено удостоверение, а да остане само проверката за "заявление за дубликат"
            /*List<Attachment> applCerts = getAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_CERTIFICATE);
            if (applCerts == null || applCerts.size() == 0) {
                String name = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentType(DocumentType.DOC_TYPE_CERTIFICATE).getName();
                errMsg = "Заявлението няма издадено " + name;
                hasError = true;
            } else*/ if (duplicateApply == null || duplicateApply.size() == 0) {
                String name = nomenclaturesDataProvider.getDocumentType(DocumentType.DOC_TYPE_DUPLICATE_APPLY).getName();
                return "Заявлението няма прикачен документ от тип: " + name;

            }

        }  else if (docTypeId == DocumentType.DOC_TYPE_CERTIFICATE_FACTUAL_ERROR || docTypeId == DOC_TYPE_DOCTORATE_CERTIFICATE_FACTUAL_ERROR) {
            List<Attachment> factualErrorApply = attachmentDataProvider.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_FACTUAL_ERROR_APPLY);
            if (factualErrorApply == null || factualErrorApply.size() == 0) {
                String name = nomenclaturesDataProvider.getDocumentType(DocumentType.DOC_TYPE_FACTUAL_ERROR_APPLY).getName();
                return "Заявлението няма прикачен документ от тип: " + name;
            }

        }
        return null;
    }
}
