package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CertificateNumberGenerationException;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.Table;
import com.nacid.bl.users.User;
import com.nacid.bl.utils.regprof.EducationTypeUtils;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.RegprofProfessionExperienceExaminationRecord;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
import com.nacid.regprof.web.model.applications.RegprofApplicationAttachmentWebModel;
import com.nacid.regprof.web.model.applications.RegprofQualExamWebModel;
import com.nacid.report.TemplateGenerator;
import com.nacid.web.MessagesBundle;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.impl.applications.FileUploadListener;
import com.nacid.web.model.common.ComboBoxWebModel;
import com.nacid.web.model.common.SystemMessageWebModel;
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

public class RegprofApplicationFinalizationHandler extends RegprofBaseAttachmentHandler {

    private ServletContext servletContext;

    private static final String NEXT_SCREEN = "regprofapplication";
    private static final String SYSTEM_MESSAGE = "finalizationMessage";
    private static final String DOCUMENT_TYPE_NAME = "documentTypeName";
    private static final String SUGGESTION_OPERATION = "suggestionOperation";
    private static final String SUGGESTION = "suggestion";
    private static final String DOC_TYPE_COMBO = "docTypeCombo";
    private static final String FINALIZATION_STATUS_COMBO = "finalizationStatusCombo";
    private static final String FINALIZATION_DOCFLOW_STATUS_COMBO = "finalizationDocflowStatusCombo";
    private static final String ENABLE_GENERATION = "enableGeneration";

    private static final String DUPLICATE_TYPE_DUPLICATE = "duplicate";
    private static final String DUPLICATE_TYPE_OBVIOUS_FACTUAL_ERROR = "obvious_factual_error";

    public RegprofApplicationFinalizationHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    @Override
    public void handleNew(HttpServletRequest request, HttpServletResponse response) {
        Integer applicationId = DataConverter.parseInteger(request.getParameter("appId"), null);
        if (applicationId == null) {
            applicationId = DataConverter.parseInteger(request.getParameter("id"), null);
            if (applicationId == null) {
                applicationId = (Integer) request.getAttribute("id");
                if (applicationId == null || applicationId < 0) {
                    throw new UnknownRecordException("Unknown regprof application ID:" + applicationId);
                }
            }
        }
        List<RegprofApplicationAttachment> factualErrors =
                getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_APPLY);

        List<Integer> certIds = new ArrayList<Integer>();
        if (factualErrors != null && factualErrors.size() > 0) {
            certIds.add(DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_CPO);
            certIds.add(DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_HIGHER_AND_SECONDARY);
            certIds.add(DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_SDK);
            certIds.add(DocumentType.DOC_TYPE_REGPROF_CERTIFICATE_STAJ);
        }

        request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL, new RegprofApplicationAttachmentWebModel(applicationId, certIds));
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_FINALIZATION_DATA);
        request.setAttribute(SUGGESTION_OPERATION, "Ново");

        RegprofApplication application = getNacidDataProvider().getRegprofApplicationDataProvider().getRegprofApplication(applicationId);
        Integer applicationStatus = application.getApplicationStatusId();
        Integer docflowStatus = application.getApplicationDetails().getDocflowStatusId();
        generateStatusCombo(applicationStatus, docflowStatus, applicationId, request);
        
        Integer activeId = null;
        String suggestion = "";
        if (ApplicationStatus.REGPROF_POSITIVE_STATUS_CODES.contains(applicationStatus)) {
            RegprofTrainingCourseDetailsBase details = application.getTrCourseDocumentPersonDetails();
            if (details.getHasExperience() == 1 && (applicationStatus == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE || details.getHasEducation() == 0)) {
                activeId = DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ;
            } else if (details.getHasEducation() == 1) {
                activeId = getDocumentTypeByInstitutionType(details.getEducationTypeId(), getNacidDataProvider().getProfessionalInstitutionDataProvider());
            } else {
                activeId = null;
            }
            suggestion = "Предложение за удостоверение";
            generateDocTypesComboBox(activeId, request);
        } else if (ApplicationStatus.REGPROF_NEGATIVE_STATUS_CODES.contains(applicationStatus)) {
            activeId = null;
            suggestion = "Предложение за отказ";
            generateDenialProposalComboBox(activeId, request);
        } else if (ApplicationStatus.APPLICATION_TERMINATED_STATUS_CODE == applicationStatus) {
            activeId = DocumentType.DOC_TYPE_REGPROF_PISMO_PREKRATIAVANE;
            suggestion = "Писмо за прекратяване";
            generateDocTypesComboBox(activeId, request);
        } else if (ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE == applicationStatus) {
            activeId = DocumentType.DOC_TYPE_REGPROF_ZAPOVED_OBEZSILVANE;
            suggestion = "Заповед за обезсилване";
            generateDocTypesComboBox(activeId, request);
        } /*else if (RegprofApplicationStatus.APPLICATION_ARCHIVED_STATUS_CODE == applicationStatus) {
            request.setAttribute("showDocuments", false);
        } */else {
            request.setAttribute("showDocuments", false);
        }
        
        request.setAttribute(SUGGESTION, suggestion);
    }

    @Override
    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {

        Integer applicationId = DataConverter.parseInt(request.getParameter("appId"), -1);
        if (applicationId < 0) {
            applicationId = DataConverter.parseInt(request.getParameter("id"), -1);
            if (applicationId < 0) {
                applicationId = (Integer) request.getAttribute("id");
                if (applicationId == null || applicationId < 0) {
                    throw new UnknownRecordException("Unknown regprof application ID:" + applicationId);
                }
            }
        }


        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDP = nacidDataProvider.getRegprofApplicationDataProvider();

        List<RegprofApplicationAttachment> list = new ArrayList<RegprofApplicationAttachment>();
        RegprofApplication application = appDP.getRegprofApplication(applicationId);
        Integer applicationStatus = application.getApplicationStatusId();
        Integer docflowStatus = application.getApplicationDetails().getDocflowStatusId();




        if (ApplicationStatus.REGPROF_POSITIVE_STATUS_CODES.contains(applicationStatus)) {

            RegprofTrainingCourseDetailsBase details = application.getTrCourseDocumentPersonDetails();
            if (details.getHasExperience() == 1 && (application.getApplicationStatusId() == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE ||
                    details.getHasEducation() == 0)) {
                list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_STAJ);
            } else if (details.getHasEducation() == 1) {
                Integer documentTypeId = getDocumentTypeByInstitutionType(details.getEducationTypeId(), nacidDataProvider.getProfessionalInstitutionDataProvider());
                list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, documentTypeId);
            }
        } else if (ApplicationStatus.REGPROF_NEGATIVE_STATUS_CODES.contains(applicationStatus)) {
            list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1);
            list.addAll(getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2));
            list.addAll(getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3));
        } else if (ApplicationStatus.APPLICATION_TERMINATED_STATUS_CODE == applicationStatus) {
            list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_PISMO_PREKRATIAVANE);
        } else if (ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE == applicationStatus) {
            list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_ZAPOVED_OBEZSILVANE);
        }


        if (ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE == docflowStatus || ApplicationDocflowStatus.APPLICATION_FINISHED_DOCFLOW_STATUS_CODE == docflowStatus) {
            list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByTypes(applicationId, DocumentType.REGPROF_SUGGESTIONS);
            request.setAttribute("disableGeneration", "disabled=\"disabled\"");
        }

        if (list == null || list.isEmpty()) {
            handleNew(request, response);
            return;
        }
        
        Collections.sort(list, new Comparator<RegprofApplicationAttachment>() {
            public int compare(RegprofApplicationAttachment att1, RegprofApplicationAttachment att2) {
                return att1.getId() - att2.getId();
            }
        });
        RegprofApplicationAttachment att = Utils.getListLastElement(list);
        
        if (att == null) {
            handleNew(request, response);
            return;
        } else {
            generateStatusCombo(applicationStatus, application.getApplicationDetails().getDocflowStatusId(), applicationId, request);
            int attachmentId = att.getId();

            request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_FINALIZATION_DATA);

            String dfUrl = getDocFlowUrl(nacidDataProvider, att.getParentId(), attachmentId, getEditUrl(applicationId));
            request.setAttribute(WebKeys.REGPROF_APPLICATION_ATTCH_WEB_MODEL, new RegprofApplicationAttachmentWebModel(att.getId(), att.getParentId(), att
                    .getDocDescr(), att.getFileName(), att.getScannedFileName(), 
                    att.getDocflowNum(), dfUrl));

            generateDocTypesComboBox(att.getDocTypeId(), request);

            boolean enableGeneration = true;
            String suggestion = "";
            String documentType = "";

            if (DocumentType.REGPROF_CERTIFICATE_SUGGESTIONS.contains(att.getDocTypeId())) {
                suggestion = "Предложение за удостоверение";
                List<Integer> certificateDocumentTypes = new ArrayList<Integer>();
                certificateDocumentTypes.addAll(DocumentType.REGPROF_CERTIFICATES);
                certificateDocumentTypes.add(DocumentType.DOC_TYPE_REGPROF_DUPLICATE_CERTIFICATE);
                certificateDocumentTypes.add(DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_CERTIFICATE);
                list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByTypes(applicationId, certificateDocumentTypes);
                if (list == null || list.isEmpty()) {
                    newSecondAttachment(request, DocumentType.REGPROF_SUGGESTIONS_TO_CERTIFICATES.get(att.getDocTypeId()));
                    documentType = "Удостоверение";
                } else {
                    RegprofApplicationAttachment certAttachment = Utils.getListLastElement(list);
                    editSecondAttachment(request, certAttachment, applicationId);
                    request.setAttribute("showDuplicateButtons", true);
                }
            } else if (DocumentType.REGPROF_DENIAL_SUGGESTIONS.contains(att.getDocTypeId())) {
                suggestion = "Предложение за отказ";
                documentType = "Отказ";
                list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_OTKAZ_1);
                list.addAll(getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_OTKAZ_2));
                list.addAll(getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_OTKAZ_3));
                if (list == null || list.isEmpty()) {
                    switch(att.getDocTypeId()) {
                        case DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_1: newSecondAttachment(request, DocumentType.DOC_TYPE_REGPROF_OTKAZ_1); break;
                        case DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_2: newSecondAttachment(request, DocumentType.DOC_TYPE_REGPROF_OTKAZ_2); break;
                        case DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_OTKAZ_3: newSecondAttachment(request, DocumentType.DOC_TYPE_REGPROF_OTKAZ_3); break;
                        default: break;
                    }
                } else {
                    editSecondAttachment(request, Utils.getListLastElement(list), applicationId);
                }
            } else if (att.getDocTypeId() == DocumentType.DOC_TYPE_REGPROF_PISMO_PREKRATIAVANE) {
                suggestion = "Писмо за прекратяване";
                enableGeneration = false;
            } else if (att.getDocTypeId() == DocumentType.DOC_TYPE_REGPROF_ZAPOVED_OBEZSILVANE) {
                suggestion = "Заповед за обезсилване";
                enableGeneration = false;
            } else {
                enableGeneration = false;
            }
            request.setAttribute(SUGGESTION, suggestion);
            request.setAttribute(DOCUMENT_TYPE_NAME, documentType);
            request.setAttribute(SUGGESTION_OPERATION, "Промяна на");
            request.setAttribute(ENABLE_GENERATION, enableGeneration);
        }
    }
    
    /**
     * 
     * @param request
     * @param docTypeId - proect za udostoverenie ili otkaz
     */
    private void newSecondAttachment(HttpServletRequest request, int docTypeId) {
        request.setAttribute("id2", 0);
        request.setAttribute("docTypeId2", docTypeId);
    }
    
    private void editSecondAttachment(HttpServletRequest request, RegprofApplicationAttachment att, int applicationId) {
        String dfUrl = getDocFlowUrl(getNacidDataProvider(), att.getParentId(), att.getId(), getEditUrl(applicationId));
        request.setAttribute("docflowUrl2", dfUrl);
        request.setAttribute("docflowNum2", att.getDocflowNum());
        request.setAttribute("id2", att.getId());
        request.setAttribute("docTypeId2", att.getDocTypeId());
        request.setAttribute("docDescr2", att.getDocDescr());
        request.setAttribute("fileName2", att.getFileName());
        request.setAttribute("scannedFileName2", att.getScannedFileName());
    }
    
    @SuppressWarnings("rawtypes")
    public void handleSave(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Integer applicationId = DataConverter.parseInteger(request.getParameter("appId"), null);
        if (applicationId == null) {
            throw new UnknownRecordException("Unknown application id");
        }
        String imiCorrespondence = DataConverter.parseString(request.getParameter("imiCorrespondence"), null);
        Integer applicationStatusId = DataConverter.parseInteger(request.getParameter("applicationStatusId"), null);
        Integer docflowStatusId = DataConverter.parseInteger(request.getParameter("docflowStatusId"), null);
        String duplicateType = DataConverter.parseString(request.getParameter("duplicate_type"), null);

        RegprofApplicationDataProvider appDP = getNacidDataProvider().getRegprofApplicationDataProvider();
        RegprofApplicationAttachmentDataProvider attDP = getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
        NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();
        if (duplicateType != null) {
            boolean hasError = false;
            String errorMessage = "";


            List<Integer> certificateDocumentTypes = new ArrayList<Integer>();
            certificateDocumentTypes.addAll(DocumentType.REGPROF_CERTIFICATES);
            certificateDocumentTypes.add(DocumentType.DOC_TYPE_REGPROF_DUPLICATE_CERTIFICATE);
            certificateDocumentTypes.add(DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_CERTIFICATE);
            List<RegprofApplicationAttachment> list = getRegprofApplicationAttachmentDataProvider().getAttachmentsForParentByTypes(applicationId, certificateDocumentTypes);
            RegprofApplicationAttachment last = Utils.getListLastElement(list);
            if (last == null) {
                hasError = true;
                errorMessage = "Заявлението няма генерирано удостоверение!";
           } else if (StringUtils.isEmpty(last.getDocflowNum())) {
                hasError = true;
                errorMessage = "Последно генерираното удосотоверение трябва да има деловоден номер!";
            } else {
                if (DUPLICATE_TYPE_OBVIOUS_FACTUAL_ERROR.equals(duplicateType)) {
                    List<RegprofApplicationAttachment> factualErrors = attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_APPLY);
                    if (factualErrors == null || factualErrors.isEmpty()) {
                        hasError = true;
                        DocumentType docType = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_APPLY);
                        errorMessage = "Няма прикачено " + docType.getName();
                    }
                } else if (DUPLICATE_TYPE_DUPLICATE.equals(duplicateType)) {
                    List<RegprofApplicationAttachment> duplicateApplies = attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_DUPLICATE_APPLY);
                    if (duplicateApplies == null || duplicateApplies.isEmpty()) {
                        hasError = true;
                        DocumentType docType = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_DUPLICATE_APPLY);
                        errorMessage = "Няма прикачено " + docType.getName();
                    }
                } else {
                    hasError = true;
                    errorMessage = "Непознат тип дубликат (" + duplicateType + ")";
                }
            }

            if (hasError) {
                request.setAttribute(SYSTEM_MESSAGE, new SystemMessageWebModel(errorMessage, SystemMessageWebModel.MESSAGE_TYPE_ERROR));
            } else {
                User user = getLoggedUser(request, response);
                if (DUPLICATE_TYPE_DUPLICATE.equals(duplicateType)) {
                    appDP.duplicateApplicationCertificate(applicationId, user.getUserId());
                    request.setAttribute(SYSTEM_MESSAGE, new SystemMessageWebModel(errorMessage, SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                } else if (DUPLICATE_TYPE_OBVIOUS_FACTUAL_ERROR.equals(duplicateType)) {
                    appDP.duplicateApplicationCertificateBecauseOfFactualError(applicationId, user.getUserId());
                }
                request.setAttribute(SYSTEM_MESSAGE, new SystemMessageWebModel("Генерирано е ново удостоверение, което се намира в прикачени документи.", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));

            }
            new RegprofApplicationHandler(servletContext).handleEdit(request, response);
            return;
        }


        if (applicationStatusId != null) {

            User user = getLoggedUser(request, response);
            String archiveNumber = "";

            
            boolean hasError = false;
            String errorMessage = "";



            if (applicationStatusId == ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE) {
                List<RegprofApplicationAttachment> invalidationRequests = attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_ISKANE_OBEZSILVANE);
                List<RegprofApplicationAttachment> courtDecisions = attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_COURT_DECISION);
                if ((invalidationRequests == null || invalidationRequests.isEmpty()) && (courtDecisions == null || courtDecisions.isEmpty())) {
                    hasError = true;
                    DocumentType invalidationRequest = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_ISKANE_OBEZSILVANE);
                    DocumentType courtDecision = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_COURT_DECISION);
                    errorMessage = "Няма прикачено " + invalidationRequest.getName() + " или " + courtDecision.getName();
                }
            } else if (applicationStatusId == ApplicationStatus.APPLICATION_VOLUNTARILY_TERMINATED_CODE) {
                List<RegprofApplicationAttachment> applies = attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_VOLUNTARILY_SUSPENSION_APPLY);
                if (applies == null || applies.isEmpty()) {
                    hasError = true;
                    DocumentType docType = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_VOLUNTARILY_SUSPENSION_APPLY);
                    errorMessage = "Няма прикачено " + docType.getName();
                }
            } else if (applicationStatusId == ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE) {
                List<RegprofApplicationAttachment> denials = attDP.getAttachmentsForParentByTypes(applicationId, DocumentType.REGPROF_DENIALS);
                if (denials == null || denials.isEmpty()) {
                    hasError = true;
                    errorMessage = "Заявлението няма издаден отказ!";
                } else {
                    boolean hasDocflow = false;
                    for (RegprofApplicationAttachment denial : denials) {
                        if (denial.getDocflowId() != null && !denial.getDocflowId().isEmpty() && denial.getDocflowDate() != null) {
                            hasDocflow = true;
                            break;
                        }
                        if (!hasDocflow) {
                            hasError = true;
                            errorMessage = "Отказът няма генериран деловоден номер!";
                        }
                    }
                }
            }




            if (docflowStatusId == ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE) {
                archiveNumber = DataConverter.parseString(request.getParameter("archive_number"), null);
            } else if (docflowStatusId == ApplicationDocflowStatus.APPLICATION_IZDADENO_DOCFLOW_STATUS_CODE) {
                List<RegprofApplicationAttachment> certificates = attDP.getAttachmentsForParentByTypes(applicationId, DocumentType.REGPROF_CERTIFICATES);
                if (certificates == null || certificates.isEmpty()) {
                    hasError = true;
                    errorMessage = "Заявлението няма издадено удостоверение!";
                } else {
                    boolean hasDocflow = false;
                    for (RegprofApplicationAttachment certificate : certificates) {
                        if (certificate.getDocflowId() != null && !certificate.getDocflowId().isEmpty() && certificate.getDocflowDate() != null) {
                            hasDocflow = true;
                            break;
                        }
                        if (!hasDocflow) {
                            hasError = true;
                            errorMessage = "Удостоверението няма генериран деловоден номер!";
                        }
                    }
                }
            }
            if (!hasError) {
                appDP.saveStatusOnly(applicationId, applicationStatusId, docflowStatusId, user.getUserId(), archiveNumber);
                request.setAttribute(SYSTEM_MESSAGE, new SystemMessageWebModel("Статусът беше променен успешно!", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            } else {
                request.setAttribute(SYSTEM_MESSAGE, new SystemMessageWebModel(errorMessage, SystemMessageWebModel.MESSAGE_TYPE_ERROR));
            }
            new RegprofApplicationHandler(servletContext).handleEdit(request, response);
            return;
        }





        if (imiCorrespondence != null) {
            RegprofApplicationDetails details = appDP.getRegprofApplication(applicationId).getApplicationDetails();            
            appDP.saveIMIOnly(details, imiCorrespondence);
            request.setAttribute(SYSTEM_MESSAGE, new SystemMessageWebModel("Преписката беше променена успешно!", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            new RegprofApplicationHandler(servletContext).handleEdit(request, response);
            return;
        }

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
            
            /** first file */
            int id = 0;
            String docDescr = null;
            Integer docTypeId = null;
            String contentType = null;
            InputStream is = null;
            String fileName = null;
            int fileSize = 0;
            InputStream scannedIs = null;
            String scannedContentType = null;
            String scannedFileName = null;
            int scannedFileSize = 0;
            String docflowUrl = null;
            /** end of first file */
            
            /** second file */
            int id2 = 0;
            String docDescr2 = null;
            Integer docTypeId2 = null;
            String contentType2 = null;
            InputStream is2 = null;
            String fileName2 = null;
            int fileSize2 = 0;
            InputStream scannedIs2 = null;
            String scannedContentType2 = null;
            String scannedFileName2 = null;
            int scannedFileSize2 = 0;
            String docflowUrl2 = null;
            String certificateNumber = null;
            /** end of second file */
            
            boolean generate = false;

            Iterator iter = uploadedItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();

                if (item.isFormField()) {

                    if (item.getFieldName().equals("id"))
                        id = DataConverter.parseInt(item.getString("UTF-8"), 0);
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
                    else if (item.getFieldName().equals("id2"))
                        id2 = DataConverter.parseInt(item.getString("UTF-8"), 0);
                    else if (item.getFieldName().equals("docDescr2"))
                        docDescr2 = item.getString("UTF-8");
                    else if (item.getFieldName().equals("docTypeId2"))
                        docTypeId2 = DataConverter.parseInteger(item.getString("UTF-8"), null);
                    else if (item.getFieldName().equals("docflowUrl2")) {
                        docflowUrl2 = item.getString("UTF-8");
                    } else if (item.getFieldName().equals("certNumber")) {
                        certificateNumber = item.getString("UTF-8");
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
                    } else if (item.getFieldName().equals("scanned_content")) {
                        scannedFileSize = (int) item.getSize();
                        if (scannedFileSize > 0) {
                            scannedIs = item.getInputStream();
                            scannedFileName = prepareFileName(item.getName());    
                            scannedContentType = item.getContentType();
                        }
                    } else if (item.getFieldName().equals("doc_content2")) {
                        fileSize2 = (int) item.getSize();
                        if (fileSize2 > 0) {
                            is2 = item.getInputStream();
                            fileName2 = prepareFileName(item.getName());
                            contentType2 = item.getContentType();
                        }
                    } else if (item.getFieldName().equals("scanned_content2")) {
                        scannedFileSize2 = (int) item.getSize();
                        if (scannedFileSize2 > 0) {
                            scannedIs2 = item.getInputStream();
                            scannedFileName2 = prepareFileName(item.getName());    
                            scannedContentType2 = item.getContentType();
                        }
                    }
                }
            }
            
            boolean generate1 = generate && id == 0;
            boolean generate2 = generate && id != 0 && id2 == 0;

            boolean hasError = saveFile(request, response, docTypeId, applicationId, certificateNumber, id, generate1, contentType, fileName, is, fileSize,
                    scannedContentType, scannedFileName, scannedIs, scannedFileSize, docDescr);
            
            if (docTypeId2 != null && !hasError) { // there might not be a second file
                hasError = saveFile(request, response, docTypeId2, applicationId, certificateNumber, id2, generate2, contentType2, fileName2, is2, fileSize2,
                    scannedContentType2, scannedFileName2, scannedIs2, scannedFileSize2, docDescr2);
            }
            
            request.setAttribute(WebKeys.APPLICATION_ID, applicationId);
            request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
            
        } catch (Exception e) {
            throw Utils.logException(this, e);
        } finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }
        
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_FINALIZATION_DATA);
        request.setAttribute("id", applicationId);
        new RegprofApplicationHandler(servletContext).handleEdit(request, response);
    }
    
    private void resetTableData(HttpServletRequest request, int applicationId) { // tova e za da se refreshne application attachments
        Table table = (Table) request.getSession().getAttribute(WebKeys.TABLE_APPLICATION_ATTCH);
        if (table == null) {
            return;
        }
        table.emtyTableData();
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationAttachmentDataProvider attDP = getRegprofApplicationAttachmentDataProvider();
        NomenclaturesDataProvider nomDP = nacidDataProvider.getNomenclaturesDataProvider();

        List<RegprofApplicationAttachment> attachments = attDP.getAttachmentsForParent(applicationId);

        if (attachments != null) {
            for (RegprofApplicationAttachment att : attachments) {

                String docType = "";
                DocumentType dt = nomDP.getDocumentType(att.getDocTypeId());
                if (dt != null) {
                    docType = dt.getLongName();
                }
                
                String fileName = att.getScannedFileName() == null ? att.getFileName() : att.getScannedFileName();
                
                try {
                    table.addRow(att.getId(), att.getDocflowNum(), docType, att.getDocDescr(), fileName, "").setDeletable(StringUtils.isEmpty(att.getDocflowNum()));
                } catch (IllegalArgumentException e) {
                    throw Utils.logException(e);
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
    }

    private static String getEditUrl(int applicationId) {
        return "/control/regprofapplication/edit?id=" + applicationId + "&activeForm=" + RegprofApplicationHandler.FORM_ID_FINALIZATION_DATA;
    }

    protected RegprofApplicationAttachmentDataProvider getRegprofApplicationAttachmentDataProvider() {
        return getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
    }

    protected int getDocTypeCategory() {
        return DocCategory.REG_PROF_SUGGESTIONS;
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
        if (activeId == null) {
            List<? extends FlatNomenclature> flatNomeclatures = null;
    
            flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentTypes(null, null, getDocTypeCategory());
    
            ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
            if (flatNomeclatures != null) {
                for (FlatNomenclature s : flatNomeclatures) {
                    combobox.addItem(s.getId() + "", ((DocumentType) s).getLongName());
                }
                request.setAttribute(DOC_TYPE_COMBO, combobox);
            }
        } else {
            NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();
            DocumentType docType = nDP.getDocumentType(activeId);
            List<DocumentType> nomenclatures = new ArrayList<DocumentType>();
            nomenclatures.add(docType);
            ComboBoxUtils.generateNomenclaturesComboBox(activeId, nomenclatures, true, request, DOC_TYPE_COMBO, false);
        }
    }

    //RayaWritten-----------------------------------------------------------------
    private void generateDenialProposalComboBox(Integer activeId, HttpServletRequest request) {
        List<? extends FlatNomenclature> flatNomeclatures = null;
        flatNomeclatures = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentTypes(null, null, getDocTypeCategory());
        ComboBoxWebModel combobox = new ComboBoxWebModel(activeId == null ? "" : activeId + "", true);
        if (flatNomeclatures != null) {
            for (FlatNomenclature s : flatNomeclatures) {
                if(DocumentType.REGPROF_DENIAL_SUGGESTIONS.contains(s.getId()))
                    combobox.addItem(s.getId() + "", ((DocumentType) s).getLongName());
            }
            request.setAttribute(DOC_TYPE_COMBO, combobox);
        }
    }
    //------------------------------------------------------------------------------------

    private void generateStatusCombo(Integer activeId, int docflowStatus, int applicationId, HttpServletRequest request) {
        NomenclaturesDataProvider nDP = getNacidDataProvider().getNomenclaturesDataProvider();
        List<ApplicationStatus> statuses = nDP.getApplicationStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, null, true);
        int currentStatusId = getNacidDataProvider().getRegprofApplicationDataProvider().getRegprofApplication(applicationId).getApplicationStatusId();

        //proverqva dali tekushtiq status sy6testvuva v statuses. Ako ne, go dobavq!
        boolean exists = false;
        for (ApplicationStatus status : statuses) {
            if (status.getId() == currentStatusId) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            ApplicationStatus status = nDP.getApplicationStatus(NumgeneratorDataProvider.REGPROF_SERIES_ID, currentStatusId);
            statuses.add(status);
        }
        //kraj na proverkata i dobavqne na tekustiq status

        ComboBoxUtils.generateNomenclaturesComboBox(activeId, statuses, false, request, FINALIZATION_STATUS_COMBO, false);


        List<ApplicationDocflowStatus> docflowStatuses = nDP.getApplicationDocflowStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(docflowStatus, docflowStatuses, false, request, FINALIZATION_DOCFLOW_STATUS_COMBO, false);
    }

    private static Integer getDocumentTypeByInstitutionType(Integer educationTypeId, ProfessionalInstitutionDataProvider pidp) {
        Integer activeId = null;

        switch(pidp.getInstitutionType(educationTypeId)) {
            case ProfessionalInstitutionDataProvider.HIGHER_INSTITUTION_TYPE:
            case ProfessionalInstitutionDataProvider.SECONDARY_PROFESSIONAL_INSTITUTION_TYPE:
                activeId = DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_HIGHER_AND_SECONDARY;
                break;
            case ProfessionalInstitutionDataProvider.PROFESSIONAL_EDUCATION_CENTER_INSTITUTION_TYPE:
                activeId = DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_CPO;
                break;
            case ProfessionalInstitutionDataProvider.SDK_INSTITUTION_TYPE:
                activeId = DocumentType.DOC_TYPE_REGPROF_PREDLOJENIE_CERTIFICATE_SDK;
                break;
            default:
                activeId = null;
        }
        return activeId;
    }
    
    /**
     * 
     * @param request
     * @param docTypeId
     * @param applicationId
     * @param certificateNumber - nomer na udostoverenie
     * @param id - id na attachment-a
     * @param generate - generira dokument ot zadaden shablon ako e true
     * @param contentType
     * @param fileName
     * @param is
     * @param fileSize
     * @param scannedContentType
     * @param scannedFileName
     * @param scannedIs
     * @param scannedFileSize
     * @param docDescr
     * @throws IOException
     */
    private boolean saveFile(HttpServletRequest request, HttpServletResponse response, Integer docTypeId, int applicationId, String certificateNumber, int id, boolean generate, String contentType, String fileName,
            InputStream is, int fileSize, String scannedContentType, String scannedFileName, InputStream scannedIs, int scannedFileSize, String docDescr) throws IOException {
        
        RegprofApplicationAttachmentDataProvider attDP = getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
        RegprofApplicationDataProvider applicationsDataProvider = getNacidDataProvider().getRegprofApplicationDataProvider();
        NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();

        RegprofApplicationAttachment attachment = null;
        RegprofApplication app = applicationsDataProvider.getRegprofApplication(applicationId);

        boolean hasError = false;
        String errMsg = "";
        UUID uuid = null;
        if (docTypeId != null && DocumentType.REGPROF_CERTIFICATE_SUGGESTIONS.contains(docTypeId) && id == 0) {
            List<RegprofApplicationAttachment> suggestions = attDP.getAttachmentsForParentByType(applicationId, docTypeId);
            if (suggestions != null && !suggestions.isEmpty()) {
                hasError = true;
                String name = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentType(docTypeId).getName();
                errMsg = "Заявлението вече има прикачено " + name;
            }
        } else if (docTypeId != null && DocumentType.REGPROF_CERTIFICATES.contains(docTypeId)) {
            List<RegprofApplicationAttachment> applCerts = attDP.getAttachmentsForParentByType(applicationId, docTypeId);
            Set<Integer> applCertIds = new HashSet<Integer>();
            if (applCerts != null) {
                for (RegprofApplicationAttachment a : applCerts) {
                    applCertIds.add(a.getId());
                }
            }
            //TODO::::
            //Ako ima ve4e vyvedeno udostoverenie i NE se editva zapisa na udostoverenieto - togava hasError stava = true
            if ((certificateNumber == null || certificateNumber.isEmpty()) && applCertIds.size() > 0 && (id == 0 || !applCertIds.contains(id))) {
                hasError = true;
                String name = getNacidDataProvider().getNomenclaturesDataProvider().getDocumentType(docTypeId).getName();
                errMsg = "Заявлението вече има издадено " + name;
            }
        }

        /** start of general validation */
        if (app.getResponsibleUsers() == null || app.getResponsibleUsers().isEmpty()) {
            hasError = true;
            errMsg += "Не е посочен отговорник. ";
        }
        if (app.getTrCourseDocumentPersonDetails().getEducationTypeId() != null) {
            if ((app.getTrCourseDocumentPersonDetails().getEducationTypeId() == EducationTypeUtils.HIGHER_EDUCATION_TYPE ||
                    app.getTrCourseDocumentPersonDetails().getEducationTypeId() == EducationTypeUtils.SDK_EDUCATION_TYPE) && 
                    app.getTrCourseDocumentPersonDetails().getHighProfQualificationId() == null) {
                hasError = true;
                errMsg += "В обучение не е посочена професионална квалификация по документи. ";
            }
            if (app.getTrCourseDocumentPersonDetails().getEducationTypeId() == EducationTypeUtils.SDK_EDUCATION_TYPE && app.getTrCourseDocumentPersonDetails().getSdkProfQualificationId() == null) {
                hasError = true;
                errMsg += "В обучение не е посочена професионална квалификация по документи за СДК. ";
            }
        }

        if (docTypeId == DocumentType.DOC_TYPE_REGPROF_PISMO_PREKRATIAVANE) {
            List<RegprofApplicationAttachment> letters = 
                    attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_LETTER_TO_APPLICANT_FOR_INFORMATION);
            if (letters != null && !letters.isEmpty()) {
                boolean flag = false;
                for (RegprofApplicationAttachment letter : letters) {
                    if (!letter.getDocflowNum().isEmpty()) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    errMsg += "Писмото до заявител с искане на информация няма генериран деловоден номер!";
                    hasError = true;
                }
            } else {
                errMsg += "Няма прикачено писмо до заявител с искане на информация!";
                hasError = true;
            }
        } else if (DocumentType.REGPROF_CERTIFICATE_SUGGESTIONS.contains(docTypeId) || DocumentType.REGPROF_CERTIFICATES.contains(docTypeId)) {
            RegprofTrainingCourseDataProvider tcDP = getNacidDataProvider().getRegprofTrainingCourseDataProvider();
            RegprofTrainingCourse trainingCourse = tcDP.getRegprofTrainingCourse(app.getId());
            
            if (app.getApplicationStatusId() == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE ||
                    app.getApplicationStatusId() == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE) {
                
                if (trainingCourse.getExperienceRecord() != null && trainingCourse.getDetails().getHasExperience() == 1) {
                    RegprofProfessionExperienceExaminationRecord record = tcDP.getProfessionExperienceExamination(trainingCourse.getExperienceRecord().getId());
                    if (record == null || record.getNotRestricted() != 1 || record.getExperienceDocumentRecognized() != 1) {
                        errMsg += "Заявлението не е с удостоверен стаж. ";
                        hasError = true;
                    }
                    if (app.getApplicationStatusId() == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE && (record == null || record.getArticleItemId() == null)) {
                        errMsg += "Липсва информация за член от директивата в проверка на стажа. ";
                        hasError = true;
                    }
                } else {
                    errMsg += "В заявлението не е посочен стаж. ";
                    hasError = true;
                }
            }
            if (app.getApplicationStatusId() == ApplicationStatus.APPLICATION_RECOGNIZED_QUALIFICATION_STATUS_CODE ||
                    app.getApplicationStatusId() == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE) {
                RegprofQualExamWebModel qualExamModel = new RegprofQualExamWebModel(getNacidDataProvider(), app.getTrCourseDocumentPersonDetails());
                if (trainingCourse.getDetails().getHasEducation() == 0) {
                    errMsg += "В заявлението не е посочено обучение.";
                    hasError = true;
                } else if (qualExamModel == null || qualExamModel.getArticleId() == null) {
                    errMsg += "Заявлението не е с удостоверена квалификация (липсва информация за член от директивата).";
                    hasError = true;
                }
            }
        } else if (docTypeId == DocumentType.DOC_TYPE_REGPROF_ZAPOVED_OBEZSILVANE) {
            RegprofCertificateNumberToAttachedDocRecord certificate = applicationsDataProvider.getCertificateNumber(applicationId, id == 0 ? 0 : null);
            if (certificate == null) {
                errMsg += "Няма издадено удостоверение към това заявление. ";
                hasError = true;
            }
            List<RegprofApplicationAttachment> annulmentRequests = attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_ISKANE_OBEZSILVANE);
            List<RegprofApplicationAttachment> courtDecisions = attDP.getAttachmentsForParentByType(applicationId, DocumentType.DOC_TYPE_REGPROF_COURT_DECISION);
            if ((annulmentRequests == null || annulmentRequests.isEmpty()) && (courtDecisions == null || courtDecisions.isEmpty())) {
                hasError = true;
                DocumentType invalidationRequest = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_ISKANE_OBEZSILVANE);
                DocumentType courtDecision = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_COURT_DECISION);
                errMsg += "Няма прикачено " + invalidationRequest.getName() + " или " + courtDecision.getName();
            }

        }
        /** end of general validation */
        
        if (!hasError) {
            attachment = attDP.getAttachment(id, false, false);
            if (id != 0 && attachment == null) {
                throw new UnknownRecordException("Unknown attachment ID:" + id);
            }
            if (generate) {
                DocumentType docType = nomDP.getDocumentType(docTypeId);
                contentType = null;
                fileName = null;
                is = null;
                fileSize = 0;
                if (docType != null && !docType.isIncoming() && docType.getDocumentTemplate() != null
                        && docType.getDocumentTemplate().length() > 0) {

                    int appStstus = app != null ? app.getApplicationStatusId() : 0;
                    if (!attDP.hasAccessToGenerateDocument(appStstus, docType.getId())) {
                        errMsg += "Заявлението не е с подходящ статус за генериране на този тип документ: " + docType.getName();
                        hasError = true;
                    }
                    if (!hasError) {
                        contentType = "application/msword";
                        fileName = app.getApplicationNumber() + "_" + docType.getDocumentTemplate() + "_" + DataConverter.formatDate(new Date())
                                + ".doc";
                        fileName = fileName.replace("/", "_");

                        //Ako trqbva da se generira udostoverenie, to pyrvo se proverqva dali moje da se generira udosoverenie za tova zaqvlenie. Ako ne moje se hvyrlq exception
                        //sydyrjasht Message, koito trqbva da se pokaje na user-a
                        try {
                            if (DocumentType.REGPROF_CERTIFICATES.contains(docTypeId)) {
                                if (StringUtils.isEmpty(certificateNumber)) {
                                    certificateNumber = applicationsDataProvider.generateCertificateNumber(applicationId);
                                }
                                uuid = UUID.randomUUID();
                                is = TemplateGenerator.generateRegprofCertificate(getNacidDataProvider(),applicationId, docType, certificateNumber, uuid);
                            } else {
                                is = TemplateGenerator.generateRegprofDocFromTemplate(getNacidDataProvider(), applicationId, docType);
                            }
                            if (docTypeId == DocumentType.DOC_TYPE_REGPROF_ZAPOVED_OBEZSILVANE) {
                                applicationsDataProvider.invalidateOldCertificateNumbers(applicationId);
                            }
                            fileSize = is.available();
                        } catch (CertificateNumberGenerationException e) {
                            hasError = true;
                            errMsg += e.getMessage();
                        }
                    }
                } else {
                    errMsg += MessagesBundle.getMessagesBundle().getValue("autogeneratedDocsConditions");
                    hasError = true;
                }
            }

            if (docTypeId == null && attachment != null)
                docTypeId = attachment.getDocTypeId();

        }

        if (id == 0 && fileSize <= 0) {
            hasError = true;
            if (errMsg == null) {
                errMsg = "Не е посочен файл";
            }
        }
        String docflowNum = attachment != null ? attachment.getDocflowNum() : null;
        if (hasError) {
            SystemMessageWebModel webmodel = new SystemMessageWebModel(errMsg, SystemMessageWebModel.MESSAGE_TYPE_ERROR);
            request.setAttribute(SYSTEM_MESSAGE, webmodel);
        }
        else if (!areFilenamesAllowed(fileName, scannedFileName, id, attDP)) {
            hasError = true;
            SystemMessageWebModel webmodel = new SystemMessageWebModel("За този документ вече е прикачен файл с това име", SystemMessageWebModel.MESSAGE_TYPE_ERROR);
            request.setAttribute(SYSTEM_MESSAGE, webmodel);
        }
        else {
            int newId = attDP.saveAttachment(id, applicationId, docDescr, 
                    docTypeId, null, 
                    attachment != null ? attachment.getDocflowId() : null, 
                    attachment != null ? attachment.getDocflowDate() : null, 
                    contentType, 
                    fileName, is, fileSize, 
                    scannedContentType, scannedFileName, 
                    scannedIs, scannedFileSize, certificateNumber, uuid, getLoggedUser(request, response).getUserId());

            request.setAttribute(SYSTEM_MESSAGE, new SystemMessageWebModel("Данните бяха въведени в базата", SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            resetTableData(request, applicationId);
        }
        return hasError;
    }

}