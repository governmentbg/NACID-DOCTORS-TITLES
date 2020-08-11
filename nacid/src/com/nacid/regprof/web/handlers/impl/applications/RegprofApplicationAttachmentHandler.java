package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CertificateNumberGenerationException;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.model.applications.RegprofApplicationAttachmentWebModel;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.FileUploadListener;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.TableWebModel;
import com.nacid.web.model.table.filters.FiltersWebModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RegprofApplicationAttachmentHandler extends RegprofBaseAttachmentHandler {
    
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_DOCFLOW_NUM = "Деловоден номер";
    private static final String COLUMN_NAME_TYPE = "Тип документ";
    private static final String COLUMN_NAME_DESC = "Описание";
    private static final String COLUMN_NAME_FILE_NAME = "Име";
    private static final String COLUMN_NAME_PREVIEW = "Preview";

    private static final String APPLICATION_PARAM = "applID";
    private static final String EDIT_SCREEN = "application_attachment_edit";

    private static final String ATTRIBUTE_NAME = RegprofApplicationImpl.class.getName();
    
    /*public static final Map<Integer, Integer> REGPROF_SUGGESTIONS_TO_CERTIFICATES = new HashMap<Integer, Integer>();
    static {
        REGPROF_SUGGESTIONS_TO_CERTIFICATES.put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_CPO);
        REGPROF_SUGGESTIONS_TO_CERTIFICATES.put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY);
        REGPROF_SUGGESTIONS_TO_CERTIFICATES.put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_SDK);
        REGPROF_SUGGESTIONS_TO_CERTIFICATES.put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ, DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_STAJ);
    }
    
    public static final Map<Integer, Integer> REGPROF_SUGGESTIONS_TO_DENIALS = new HashMap<Integer, Integer>();
    static {
        REGPROF_SUGGESTIONS_TO_DENIALS.put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1, DocumentType.DOC_TYPE_REGPROF_OTKAZ_1);
        REGPROF_SUGGESTIONS_TO_DENIALS.put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2, DocumentType.DOC_TYPE_REGPROF_OTKAZ_2);
        REGPROF_SUGGESTIONS_TO_DENIALS.put(DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3, DocumentType.DOC_TYPE_REGPROF_OTKAZ_3);
    }*/

    public RegprofApplicationAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
    }
    
    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInteger(request.getParameter(APPLICATION_PARAM), null);
        if (applicationId == null) {
            throw new UnknownRecordException("Unknown regprof application ID:" + applicationId);
        }
        List<Integer> certIds = new ArrayList<Integer>();
        request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL, new RegprofApplicationAttachmentWebModel(applicationId, certIds));
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_ATTACHMENTS_DATA);

        generateDocTypesComboBox(null, request);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_ATTACHMENTS_DATA); 
        NacidDataProvider nacidDataProvider = getNacidDataProvider();

        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();

        RegprofApplicationAttachment att = attDP.getAttachment(attachmentId, false, false);

        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }

        String dfUrl = getDocFlowUrl(nacidDataProvider, att.getParentId(), attachmentId, 
                getEditUrl(attachmentId));
        request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL, new RegprofApplicationAttachmentWebModel(att.getId(), att.getParentId(), att
                .getDocDescr(), att.getFileName(), att.getScannedFileName(), 
                att.getDocflowNum(),
                dfUrl));

        generateDocTypesComboBox(att.getDocTypeId(), request);
    }

    public void handleList(HttpServletRequest request, HttpServletResponse response) {

        RegprofApplication app = (RegprofApplicationImpl) request.getAttribute(ATTRIBUTE_NAME);
        int applicationId = app.getId();
        String operation = getOperationName(request);
        if (operation.equals("save")) {
            operation = "edit";
        }
        String group = getGroupName(request);
        String query = "activeForm=" + RegprofApplicationHandler.FORM_ID_ATTACHMENTS_DATA + "&id=" + applicationId;
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
        RegprofApplicationDataProvider applicationsDataProvider = getNacidDataProvider().getRegprofApplicationDataProvider();
        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();
        
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

        List uploadedItems = null;

        try {
            // iterate over all uploaded files
            uploadedItems = upload.parseRequest(request);

            int id = 0;
            int applicationId = 0;
            String docDescr = null;
            Integer docTypeId = null;
            String contentType = null;
            InputStream is = null;
            String fileName = null;
            int copyTypeId = 0;
            int fileSize = 0;
            String certificateNumber = null;
            UUID uuid = null;
            InputStream scannedIs = null;
            String scannedContentType = null;
            String scannedFileName = null;
            int scannedFileSize = 0;
            
            String docflowUrl = null;
            
            boolean generate = false;

            Iterator iter = uploadedItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    if (item.getFieldName().equals("id"))
                        id = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("applicationId"))
                        applicationId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("docDescr"))
                        docDescr = item.getString("UTF-8");
                    else if (item.getFieldName().equals("docTypeId"))
                        docTypeId = DataConverter.parseInteger(item.getString("UTF-8"), null);
                    else if (item.getFieldName().equals("generate")) {
                        generate = DataConverter.parseBoolean(item.getString("UTF-8"));
                    }
                    else if (item.getFieldName().equals("docflowUrl")) {
                        docflowUrl = item.getString("UTF-8");
                    }
                    else if (item.getFieldName().equals("certNumber")) {
                        certificateNumber = DataConverter.parseString(item.getString("UTF-8"), null);
                        if (!StringUtils.isEmpty(certificateNumber)) {
                            if (!certificateNumber.contains("/")) {
                                certificateNumber += "/" + DataConverter.formatDate(new Date());    
                            }
                        }
                    } 
                } else {
                    if (item.getFieldName().equals("doc_content")) {
                        fileSize = (int) item.getSize();
                        if (fileSize > 0) {
                            is = item.getInputStream();
                            fileName = prepareFileName(item.getName());
                            contentType = item.getContentType();
                        }
                    }
                    else if (item.getFieldName().equals("scanned_content")) {
                        scannedFileSize = (int) item.getSize();
                        if (scannedFileSize > 0) {
                            scannedIs = item.getInputStream();
                            scannedFileName = prepareFileName(item.getName());    
                            scannedContentType = item.getContentType();
                        }
                    }
                }
            }
            boolean hasError = false;
            String errMsg = null;
            if (docTypeId != null && DocumentType.REGPROF_CERTIFICATES.contains(docTypeId)) {
                List<RegprofApplicationAttachment> applCerts = attDP.getAttachmentsForParentByType(applicationId, docTypeId);
                Set<Integer> applCertIds = new HashSet<Integer>();
                if (applCerts != null) {
                    for (RegprofApplicationAttachment a:applCerts) {
                        applCertIds.add(a.getId());
                    }
                }
                //Ako ima ve4e vyvedeno udostoverenie i NE se editva zapisa na udostoverenieto - togava hasError stava = true
                //2011.01.18 - priemam 4e ako se vyvejda vtoro zaqvlenie, to 100% certificateNumber e popylneno, t.e. ne e presko4ena javaScript proverkata za setvane na toq parametyr
                if ((certificateNumber == null || certificateNumber.isEmpty()) && applCertIds.size() > 0 && (id == 0 || !applCertIds.contains(id))) {
                    hasError = true;
                    String name = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentType(docTypeId).getName();
                    errMsg = "Заявлението вече има издадено " + name;                    
                }
            }

            RegprofApplicationAttachment attachment = null;
            RegprofApplication app = applicationsDataProvider.getRegprofApplication(applicationId);
            if (!hasError) {
                attachment = attDP.getAttachment(id, false, false);
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

                        int appStatus = app != null ? app.getApplicationStatusId() : 0;
                        if (!attDP.hasAccessToGenerateDocument(appStatus, docType.getId())) {
                            errMsg = "Заявлението не е с подходящ статус за генериране на този тип документ";
                            hasError = true;
                        } else if (DocumentType.REGPROF_CERTIFICATES.contains(docType.getId()) && hasError) {
                            String name = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentType(docType.getId()).getName();
                            errMsg = "Заявлението вече има издадено " + name;
                            hasError = true;
                        }
                        if (!hasError) {
                            contentType = "application/msword";
                            fileName = app.getApplicationNumber() + "_" + docType.getDocumentTemplate() + "_" + DataConverter.formatDate(new Date()) + ".doc";
                            fileName = fileName.replace("/", "_");
                            
                            //Ako trqbva da se generira udostoverenie, to pyrvo se proverqva dali moje da se generira udosoverenie za tova zaqvlenie. Ako ne moje se hvyrlq exception
                            //sydyrjasht Message, koito trqbva da se pokaje na user-a
                            try {
                                if (id == 0 &&  DocumentType.REGPROF_CERTIFICATES.contains(docTypeId) && !hasError && certificateNumber == null) {
                                    certificateNumber = applicationsDataProvider.generateCertificateNumber(applicationId);
                                    uuid = UUID.randomUUID();
                                    is = TemplateGenerator.generateRegprofCertificate(getNacidDataProvider(), applicationId, docType, certificateNumber, uuid);
                                } else {
                                    is = TemplateGenerator.generateRegprofDocFromTemplate(getNacidDataProvider(), applicationId, docType);
                                }
                                fileSize = is.available();
                            } catch (CertificateNumberGenerationException e) {
                                hasError = true;
                                errMsg = e.getMessage();
                            }
                        }
                    } else {
                        errMsg = MessagesBundle.getMessagesBundle().getValue("autogeneratedDocsConditions");
                        hasError = true;
                    }
                }

                if (docTypeId == null && attachment != null)
                    docTypeId = attachment.getDocTypeId();

            }
            
            if (id == 0 && fileSize <= 0) {
                hasError = true;
                if(errMsg == null) {
                    errMsg = "Не е посочен файл";
                }
            }
            String docflowNum = attachment != null ? attachment.getDocflowNum() : null;
            if (hasError) {
                SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL, 
                        new RegprofApplicationAttachmentWebModel(id, applicationId, docDescr, fileName, scannedFileName, docflowNum, docflowUrl));
            }
            else if (!areFilenamesAllowed(fileName, scannedFileName, id, attDP)) {
                if (id != 0) {
                    RegprofApplicationAttachment att = attDP.getAttachment(id, false, false);
                    if (att == null) {
                        id = 0;
                    } else {
                        fileName = att.getFileName();
                        scannedFileName = att.getScannedFileName();
                    }
                }
                SystemMessageWebModel webmodel = new SystemMessageWebModel("За този документ вече е прикачен файл с това име");
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL, 
                        new RegprofApplicationAttachmentWebModel(id, applicationId, docDescr, fileName, scannedFileName, docflowNum, docflowUrl));
            }
            else {
                int newId = attDP.saveAttachment(id, applicationId, docDescr, 
                        docTypeId, copyTypeId, 
                        attachment != null ? attachment.getDocflowId() : null, 
                        attachment != null ? attachment.getDocflowDate() : null, 
                        contentType, 
                        fileName, is, fileSize, 
                        scannedContentType, scannedFileName, 
                        scannedIs, scannedFileSize, certificateNumber, uuid, getLoggedUser(request, response).getUserId());
               
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата",
                        SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
                resetTableData(request, applicationId);
                attachment = attDP.getAttachment(newId, false, false);

                docflowUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, newId, 
                        getEditUrl(newId));
                
                request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL,
                        new RegprofApplicationAttachmentWebModel(newId, applicationId, docDescr, attachment.getFileName(), attachment.getScannedFileName(), attachment.getDocflowNum(), docflowUrl));
            }

            generateDocTypesComboBox(docTypeId, request);

            request.setAttribute(WebKeys.APPLICATION_ID, app.getId());
            
        } catch (Exception e) {
            throw Utils.logException(this, e);
        } finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }
        
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_ATTACHMENTS_DATA); 
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        request.getSession().removeAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int attId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attId <= 0) {
            throw new UnknownRecordException("Unknown attachment Id:" + attId);
        }

        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();
        RegprofApplicationAttachment att = attDP.getAttachment(attId, false, false);
        attDP.deleteAttachment(attId);
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_ATTACHMENTS_DATA); 
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
        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();

        List<RegprofApplicationAttachment> attachments = attDP.getAttachmentsForParent(applicationId);
        List<RegprofApplicationAttachment> certificates = attDP.getAttachmentsForParentByTypes(applicationId, DocumentType.REGPROF_CERTIFICATES);
        Set<Integer> certificateTypeIds = new HashSet<Integer>();
        if (certificates != null && !certificates.isEmpty()) {
            for (RegprofApplicationAttachment cert : certificates) {
                certificateTypeIds.add(cert.getDocTypeId());
            }
        }
        List<RegprofApplicationAttachment> denials = attDP.getAttachmentsForParentByTypes(applicationId, DocumentType.REGPROF_DENIALS);
        Set<Integer> denialTypeIds = new HashSet<Integer>();
        if (denials != null && !denials.isEmpty()) {
            for (RegprofApplicationAttachment denial : denials) {
                denialTypeIds.add(denial.getDocTypeId());
            }
        }

        if (attachments != null) {
            for (RegprofApplicationAttachment att : attachments) {
                String docType = "";
                int docTypeId = att.getDocTypeId();
                DocumentType dt = nomDP.getDocumentType(att.getDocTypeId());
                if (dt != null) {
                    docType = dt.getLongName();
                }
                
                String fileName = att.getScannedFileName() == null ? att.getFileName() : att.getScannedFileName();
                
                try {
                    boolean notDeletable = 
                            (DocumentType.REGPROF_CERTIFICATE_SUGGESTIONS.contains(att.getDocTypeId()) && 
                                    (certificateTypeIds.contains(DocumentType.REGPROF_SUGGESTIONS_TO_CERTIFICATES.get(docTypeId)))) ||
                                    (DocumentType.REGPROF_DENIAL_SUGGESTIONS.contains(docTypeId) && denialTypeIds.contains(DocumentType.REGPROF_SUGGESTIONS_TO_DENIALS.get(docTypeId)));
                    table.addRow(att.getId(), att.getDocflowNum(), docType, att.getDocDescr(), fileName, "").setDeletable(StringUtils.isEmpty(att.getDocflowNum()) && !notDeletable);
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }    
    
    private static String getEditUrl(int id) {
        return "/control/application_attachment/edit?id=" + id;
    }

    protected RegprofApplicationAttachmentDataProvider getRegprofApplicationAttachmentDataProvider() {
        return getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
    }

    protected int getDocTypeCategory() {
        return DocCategory.REG_PROF_APPLICATION_ATTACHMENTS;
    }
    
    /**
     * generira applicationAttachment za dadeniq documentType i Application, kato zapisva zapisa v bazata i vry6ta ID-to na zapisaniq zapis!
     * zasega se polzva v Commission*Handlers
     * @param nacidDataProvider
     * @param application
     * @param documentType
     * @return idto na zapisnaiq v bazata zapis
     */
    public static int generateAndSaveApplicationAttachment(NacidDataProvider nacidDataProvider, RegprofApplicationDetailsForReport application, DocumentType documentType, int userCreated) {
        InputStream is = TemplateGenerator.generateDocFromTemplate(nacidDataProvider, application, documentType);
        
        String fileName = application.getApplicationNumber() + "_" 
            + documentType.getDocumentTemplate()  + "_"
            + DataConverter.formatDate(new Date()) + ".doc";
        
        try {
            return nacidDataProvider.getRegprofApplicationAttachmentDataProvider().saveAttachment(0, application.getApplication().getId(), null, 
                    documentType.getId(), null, 
                    null, null, 
                    "application/msword", fileName, 
                    is, is.available(), 
                    null, null, 
                    null, 0, userCreated);
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }
    
    protected void generateDocTypesComboBox(Integer activeId, HttpServletRequest request) {
        
        if (activeId != null && (DocumentType.REGPROF_SUGGESTIONS.contains(activeId) || 
                                 DocumentType.REGPROF_CERTIFICATES.contains(activeId) || 
                                 DocumentType.REGPROF_DENIALS.contains(activeId))) {
            FlatNomenclature docType = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentType(activeId);
            ComboBoxUtils.generateNomenclaturesComboBox(activeId, Arrays.asList(docType), false, request, "docTypeCombo", false);
        } else {
            List<? extends FlatNomenclature> flatNomeclatures = null;
            
            flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider()
                .getDocumentTypes(null, null, getDocTypeCategory());
    
            ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
            if (flatNomeclatures != null) {
                for (FlatNomenclature s : flatNomeclatures) {
                    if (!DocumentType.REGPROF_CERTIFICATES.contains(s.getId()) && !DocumentType.REGPROF_DENIALS.contains(s.getId()) && s.getId() != DocumentType.DOC_TYPE_REGPROF_PISMO_PREKRATIAVANE &&
                            s.getId() != DocumentType.DOC_TYPE_REGPROF_ZAPOVED_OBEZSILVANE) {
                        combobox.addItem(s.getId() + "", ((DocumentType) s).getLongName());
                    }
                }
                request.setAttribute("docTypeCombo", combobox);
            }
        }
    }
    
}