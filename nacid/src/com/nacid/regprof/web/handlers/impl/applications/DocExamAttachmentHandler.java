package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.table.*;
import com.nacid.data.DataConverter;
import com.nacid.regprof.web.model.applications.DocExamAttachmentWebModel;
import com.nacid.regprof.web.model.applications.RegprofApplicationAttachmentWebModel;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.RequestParametersUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.FileUploadListener;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DocExamAttachmentHandler extends RegprofBaseAttachmentHandler {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_FILE_NAME = "Име";
    private static final String COLUMN_NAME_DESC = "Описание";
    private static final String COLUMN_NAME_PREVIEW = "Preview";

    private static final String COLUMN_NAME_DOCFLOW_NUM = "Деловоден номер";
    private static final String COLUMN_NAME_DOCUMENT_TYPE = "Тип документ";

    private static final String DOC_EXAM_PARAM = "docExamId";
    private static final String DOC_TYPE_PARAM = "docTypeId";

    private static final String EDIT_SCREEN = "doc_exam_attachment_edit";

    private static final String TABLE_PREFIX = "docExamAttch";

    public DocExamAttachmentHandler(ServletContext servletContext) {
        super(servletContext);
    }
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        int docExamId = DataConverter.parseInt(request.getParameter(DOC_EXAM_PARAM), 0);
        if(docExamId < 0){
            throw new UnknownRecordException("Unknown document examination ID:" + docExamId);
        }
        Integer appId = DataConverter.parseInteger(request.getParameter("appId"),null);
        if(appId == null){
            appId = DataConverter.parseInteger(request.getParameter("id"),null);
            if(appId == null){
                throw new UnknownRecordException("Unknown regprof application ID:" + appId);
            }
        }
        request.setAttribute(WebKeys.DOC_EXAM_ATTCH_WEB_MODEL, new DocExamAttachmentWebModel( docExamId, DataConverter.formatInteger(appId)));

        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);       
        super.generateDocTypesComboBox(null, request);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {
        int attachmentId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attachmentId <= 0) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }
        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);

        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();

        RegprofApplicationAttachment att = attDP.getAttachment(attachmentId, false, false);

        if (att == null) {
            throw new UnknownRecordException("Unknown attachment ID:" + attachmentId);
        }

        generateDocTypesComboBox(att.getDocTypeId(), request);

        int appId = DataConverter.parseInt(request.getParameter("appId"), 0);

        String dfUrl = getDocFlowUrl(getNacidDataProvider(), appId, attachmentId, 
                getEditUrl(attachmentId, appId + ""));

        request.setAttribute(WebKeys.DOC_EXAM_ATTCH_WEB_MODEL, 
                new DocExamAttachmentWebModel(att.getId(), att.getParentId(),
                        att.getDocDescr(), att.getFileName(), 
                        appId + "",
                        att.getScannedFileName(), att.getDocflowNum(), dfUrl));

    }

    @Override
    public void handleList(HttpServletRequest request, HttpServletResponse response) {

        Integer appId = DataConverter.parseInt(request.getParameter("appId"), -1);
        if(appId < 0){
            appId = DataConverter.parseInt(request.getParameter("id"), -1);
            if(appId < 0){
                appId = (Integer) request.getAttribute("id");
                if (appId < 0 || appId == null) {
                    throw new UnknownRecordException("Unknown regprof application ID:" + appId);
                }
            }
        }

        RegprofApplication app = getNacidDataProvider().getRegprofApplicationDataProvider().getRegprofApplication(appId);
        RegprofTrainingCourseDetailsBase trainingCourse = getNacidDataProvider().getRegprofTrainingCourseDataProvider().getRegprofTrainingCourseDetails(
                app.getApplicationDetails().getTrainingCourseId());
        DocumentExamination docExam = getNacidDataProvider().getRegprofApplicationDataProvider().getDocumentExaminationForTrainingCourse(trainingCourse.getId());
        Integer docExamId = docExam.getId();
        String operation = getOperationName(request);
        if(operation.equals("save")) {
            operation = "edit";
        }
        String group = getGroupName(request);
        String query = "activeForm=" + RegprofApplicationHandler.FORM_ID_EXAMINATION_DATA + "&id=" + appId;
        request.getSession().setAttribute("backUrlDocExam", 
                request.getContextPath() 
                + "/control" 
                + "/" + group 
                + "/" + operation
                + "?" + query);

        HttpSession session = request.getSession();
        Table table = (Table) session.getAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH);

        //boolean reloadTable = RequestParametersUtils.getParameterReloadTable(request, TABLE_PREFIX);
        boolean reloadTable = true;
        if (reloadTable || table == null) {
            TableFactory tableFactory = TableFactory.getInstance();
            table = tableFactory.createTable();

            table.addColumnHeader(COLUMN_NAME_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
            table.addColumnHeader(COLUMN_NAME_DOCFLOW_NUM, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DOCUMENT_TYPE, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_FILE_NAME, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_DESC, CellValueDef.CELL_VALUE_TYPE_STRING);
            table.addColumnHeader(COLUMN_NAME_PREVIEW, CellValueDef.CELL_VALUE_TYPE_STRING);

            //table.addColumnHeader(COLUMN_NAME_WEB_SITE, CellValueDef.CELL_VALUE_TYPE_STRING);
            //table.addColumnHeader(COLUMN_NAME_URL, CellValueDef.CELL_VALUE_TYPE_STRING);

            session.setAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH, table);
            resetTableData(request, docExamId); 
        }

        // TableState settings
        TableState tableState = (TableState) session
        .getAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH + WebKeys.TABLE_STATE);
        /**
         * ako tableState-a e null ili ne e kazano v request-a che trqbva da se
         * vzeme posledniq tableState, togava se generira nov tableState!
         */
        boolean getLastTableState = RequestParametersUtils.getParameterGetLastTableState(request, TABLE_PREFIX);
        if (tableState == null || !getLastTableState) {
            tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, TABLE_PREFIX);
            session.setAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH + WebKeys.TABLE_STATE, tableState);
        }

        // TableWebModel SETTINGS
        TableWebModel webmodel = new TableWebModel("Списък на прикачените документи");
        webmodel.setViewOpenInNewWindow(true);
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_NEW, DOC_EXAM_PARAM, docExamId + "");
        webmodel.addRequestParam(TableWebModel.OPERATION_NAME_ALL, 
                "appId", appId + "");
        // webmodel.setColumnFormatter("userDate",
        // CellFormatter.DATE_TIME_FORMATTER);
        webmodel.setGroupName("doc_exam_attachment");
        webmodel.insertTableData(table, tableState);
        request.setAttribute("docExamTableWebModel", webmodel);
        //request.setAttribute(WebKeys.NEXT_SCREEN, "diploma_type_attachment_list");

        // Generating filters for displaying to user
        FiltersWebModel filtersWebModel = (FiltersWebModel) session
        .getAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH + WebKeys.FILTER_WEB_MODEL);
        /**
         * ako filtersWebModel-a e null ili ne e kazano v request-a che trqbva
         * da se vzeme posledniq tableState(filterWebModel), togava se generira
         * nov i se slaga v sesiqta!
         */
        if (filtersWebModel == null || !getLastTableState) {
            filtersWebModel = new FiltersWebModel();

            session.setAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH
                    + WebKeys.FILTER_WEB_MODEL, filtersWebModel);
        }
        request.setAttribute(WebKeys.TABLE_FILTERS_WEB_MODEL, filtersWebModel);
    }

    @SuppressWarnings("rawtypes")
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {


        HttpSession session = request.getSession();

        // create file upload factory and upload servlet
        DiskFileItemFactory factory = new DiskFileItemFactory();

        FileCleaningTracker pTracker = FileCleanerCleanup
        .getFileCleaningTracker(getServletContext());
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

            InputStream is = null;
            String docDescr = null;
            Integer docTypeId = null;
            int docExamId = 0;
            int id = 0;
            String fileName = null;
            String contentType = null;
            int fileSize = 0;
            boolean generate = false;
            int applicationId = 0;

            InputStream scannedIs = null;
            String scannedContentType = null;
            String scannedFileName = null;
            int scannedFileSize = 0;


            String docflowUrl = null;

            Iterator iter = uploadedItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {
                    if(item.getFieldName().equals("docDescr"))
                        docDescr = item.getString("UTF-8");
                    else if(item.getFieldName().equals("id"))
                        id = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if(item.getFieldName().equals("docExamId"))
                        docExamId = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if(item.getFieldName().equals("docTypeId"))
                        docTypeId = DataConverter.parseInteger(item.getString("UTF-8"), null);
                    else if (item.getFieldName().equals("generate")) {
                        generate = DataConverter.parseBoolean(item.getString("UTF-8"));
                    }
                    else if (item.getFieldName().equals("appId")) {
                        applicationId = Integer.parseInt(item.getString("UTF-8"));
                    }
                    else if (item.getFieldName().equals("docflowUrl")) {
                        docflowUrl = item.getString("UTF-8");
                    }
                } 
                else {
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

            RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();

            RegprofApplicationAttachment attachment = attDP.getAttachment(id, false, false);
            if (id != 0 && attachment == null) {
                throw new UnknownRecordException("Unknown attachment ID:" + id);
            }

            if(generate) {
                NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
                DocumentType docType = nomDP.getDocumentType(docTypeId);
                if(docType != null && !docType.isIncoming() 
                        && docType.getDocumentTemplate() != null 
                        && docType.getDocumentTemplate().length() > 0) {

                    contentType = "application/msword";
                    RegprofApplication appl = getNacidDataProvider().getRegprofApplicationDataProvider().getRegprofApplication(applicationId);
                    fileName = appl.getApplicationNumber() + "_" 
                    +docType.getDocumentTemplate()  + "_"
                    + DataConverter.formatDate(new Date()) + ".doc";
                    fileName = fileName.replace("/", "_");
                    is = TemplateGenerator.generateRegprofDocFromTemplate(getNacidDataProvider(), applicationId, docType);
                    fileSize = is.available();
                }
                else {
                    contentType = null;
                    fileName = null;
                    is = null;
                    fileSize = 0;
                }
            }

            String docflowNum = attachment != null ? attachment.getDocflowNum() : null;
            if (id == 0 && fileSize <= 0) {
                String errMsg = "Не е посочен файл";
                if(generate) {
                    errMsg = MessagesBundle.getMessagesBundle().getValue("autogeneratedDocsConditions");
                }
                SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.DOC_EXAM_ATTCH_WEB_MODEL, 
                        new DocExamAttachmentWebModel(0, docExamId, docDescr, 
                                "", applicationId+"", null, docflowNum, docflowUrl));

            }
            else if(!areFilenamesAllowed(fileName, scannedFileName, id, getRegprofApplicationAttachmentDataProvider())) {
                SystemMessageWebModel webmodel = new SystemMessageWebModel("За този документ вече е прикачен файл с това име");
                request.setAttribute(WebKeys.SYSTEM_MESSAGE, webmodel);
                request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL , 
                        new RegprofApplicationAttachmentWebModel(0, applicationId, docDescr, "", null, docflowNum, docflowUrl));
            }
            else {

                if(docTypeId == null && attachment != null) docTypeId = attachment.getDocTypeId();

                int newId = attDP.saveAttachment(id, docExamId, docDescr, docTypeId,
                        null, 
                        attachment != null ? attachment.getDocflowId() : null, 
                                attachment != null ? attachment.getDocflowDate() : null, 
                                        contentType, fileName, is, fileSize, 
                                        scannedContentType, scannedFileName, scannedIs, scannedFileSize, getLoggedUser(request, response).getUserId());
                attachment = attDP.getAttachment(newId, false, false);
                request.setAttribute(WebKeys.SYSTEM_MESSAGE,
                        new SystemMessageWebModel("Данните бяха въведени в базата",
                                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
                resetTableData(request, docExamId);


                docflowUrl = getDocFlowUrl(getNacidDataProvider(), applicationId, newId, 
                        getEditUrl(newId, applicationId + ""));


                request.setAttribute(WebKeys.DOC_EXAM_ATTCH_WEB_MODEL, 
                        new DocExamAttachmentWebModel(
                                newId, docExamId, docDescr, 
                                attachment.getFileName(), applicationId+"",
                                attachment.getScannedFileName(),
                                attachment.getDocflowNum(),
                                docflowUrl));

            }
            generateDocTypesComboBox(docTypeId, request);

        } catch (Exception e) {
            throw Utils.logException(this, e);
        }
        finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }

        request.setAttribute(WebKeys.NEXT_SCREEN, EDIT_SCREEN);
        request.getSession().removeAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH);

    }

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        int attId = DataConverter.parseInt(request.getParameter("id"), -1);
        if (attId <= 0) {
            throw new UnknownRecordException("Unknown attachment Id:" + attId);
        }

        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();
        RegprofApplicationAttachment att = attDP.getAttachment(attId, false, false);
        request.setAttribute(WebKeys.APPLICATION_ID, att.getParentId());
        attDP.deleteAttachment(attId);

        request.getSession().removeAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH);
        //handleList(request, response);
        try {
            response.sendRedirect((String) request.getSession().getAttribute("backUrlDocExam"));
        } catch (IOException e) {
            throw Utils.logException(e);
        }
    }

    private void resetTableData(HttpServletRequest request, int docExamId) { 
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_DOC_EXAM_ATTCH);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();
        List<RegprofApplicationAttachment> attachments = attDP.getAttachmentsForParent(docExamId);
        if (attachments != null) {
            for (RegprofApplicationAttachment att : attachments) {
                if (att.getDocTypeId() != DocumentType.DOC_TYPE_REGPROF_SCREENSHOT) {
                    String fileName = att.getScannedFileName() == null ? att.getFileName() : att.getScannedFileName();
                    try {
                        NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
                        DocumentType documentType = nomDP.getDocumentType(att.getDocTypeId());
                        String docType = documentType == null ? "": documentType.getLongName();
    
                        table.addRow(att.getId(), att.getDocflowNum(), docType, fileName,
                                att.getDocDescr(), "")
                                .setDeletable(StringUtils.isEmpty(att.getDocflowNum()));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (CellCreationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static String getEditUrl(int id, String applId) {
        return "/control/doc_exam_attachment/edit?id=" + id
        + "&appId" + "=" + applId;
    }

    @Override
    protected int getDocTypeCategory() {
        return DocCategory.REG_PROF_DOCUMENT_EXAMINATIONS;
    }
    @Override
    protected RegprofApplicationAttachmentDataProvider getRegprofApplicationAttachmentDataProvider() {
        return getNacidDataProvider().getDocExamAttachmentDataProvider();
    }
}