package com.nacid.regprof.web.handlers.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.applications.regprof.DocumentExaminationImpl;
import com.nacid.bl.impl.applications.regprof.DocumentExaminationSourceImpl;
import com.nacid.bl.impl.applications.regprof.RegprofQualificationExaminationImpl;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseImpl;
import com.nacid.bl.impl.regprof.ProfessionalInstitutionValidityImpl;
import com.nacid.bl.impl.regprof.RegulatedProfessionValidityImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.EducationType;
import com.nacid.bl.nomenclatures.regprof.RegprofArticleItem;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitutionValidity;
import com.nacid.bl.table.*;
import com.nacid.bl.users.User;
import com.nacid.data.DataConverter;
import com.nacid.data.regprof.RegprofProfessionExperienceExaminationRecord;
import com.nacid.data.regprof.applications.RegprofAppDocflowStatusHistoryRecordExtended;
import com.nacid.data.regprof.applications.RegprofAppStatusHistoryForListRecord;
import com.nacid.regprof.web.handlers.RegProfBaseRequestHandler;
import com.nacid.regprof.web.handlers.impl.ajax.RegprofArticleItemsAjaxHandler;
import com.nacid.regprof.web.model.applications.RegprofProfInstExamWebModel;
import com.nacid.regprof.web.model.applications.RegprofQualExamWebModel;
import com.nacid.web.WebKeys;
import com.nacid.web.exceptions.UnknownRecordException;
import com.nacid.web.handlers.ComboBoxUtils;
import com.nacid.web.handlers.impl.TableStateAndFiltersUtils;
import com.nacid.web.handlers.impl.applications.FileUploadListener;
import com.nacid.web.model.common.SystemMessageWebModel;
import com.nacid.web.model.table.CellFormatter;
import com.nacid.web.model.table.TableWebModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//RayaWritten--------------------------------------------------------------------------
public class RegprofApplicationExaminationHandler extends RegProfBaseRequestHandler {

    //Institution validation related constants
    private static final String COLUMN_NAME_INST_VALIDITY_ID = "validityId";
    private static final String COLUMN_NAME_VALIDITY_CHECKED = "Маркирана";
    private static final String COLUMN_NAME_EXAMINATION_DATE = "Дата";
    private static final String COLUMN_NAME_VALID = "Легитимна";
    private static final String COLUMN_NAME_HAS_RIGHTS = "Има право да пров. обуч.";
    private static final String COLUMN_NAME_VALIDITY_NOTES = "Забележки";

    private static final String VALIDITY_LIST_PREFIX = "validitiesTable";

    //attributes
    private static final String ATTRIBUTE_NAME = RegprofTrainingCourseImpl.class.getName();
    //Regulated validation related constants
    private static final String COLUMN_NAME_REG_VALIDITY_ID = "regValidityId";
    private static final String COLUMN_NAME_REGULATED = "Регулирана";
    private static final String COLUMN_NAME_PROFESSION = "Професия";
    private static final String VALIDITY_REG_LIST_PREFIX = "regulatedValiditiesTable";

    private static final String NEXT_SCREEN = "regprofapplication";
    private static final String ARCHIVE_NUMBER_PREFIX = "ИД-05-04-";

    private static final int SOURCE_REGISTER = 1;
    private static final int SOURCE_COMMUNICATED_INSTITUTION = 2;

    private ServletContext servletContext;

    public RegprofApplicationExaminationHandler(ServletContext servletContext) {
        super(servletContext);
        this.servletContext = servletContext;
    }

    public void handleEdit(HttpServletRequest request, HttpServletResponse response) {        
        Integer appId = DataConverter.parseInt(request.getParameter("appId"), -1);
        if (appId < 0) {
            appId = DataConverter.parseInt(request.getParameter("id"), -1);
            if (appId < 0) {
                appId = (Integer) request.getAttribute("id");
                if (appId < 0 || appId == null) {
                    throw new UnknownRecordException("Unknown regprof application ID:" + appId);
                }
            }
        }

        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();            
        RegprofApplication appRecord = appDp.getRegprofApplication(appId);
        RegprofTrainingCourse trainingCourse = (RegprofTrainingCourse) request.getAttribute(ATTRIBUTE_NAME);

        makeStatusSettings(request, trainingCourse, appRecord);

        makeExperienceSettings(request, response, trainingCourse);

        makeInstitutionSettings(request, trainingCourse, appRecord);

        makeDocumentSettings(request, trainingCourse, response);

        makeQualificationSettings(request, trainingCourse, response);

        makeRegulatedSettings(request, appId);

        String navetWebSite = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue("navetWebSite");
        request.setAttribute("navetWebSite", navetWebSite);
        String mlspWebSite = nacidDataProvider.getUtilsDataProvider().getCommonVariableValue("mlspWebSite");
        request.setAttribute("mlspWebSite", mlspWebSite);

        request.setAttribute("id", appId);
    }

    public void handleSave(HttpServletRequest request, HttpServletResponse response){
        int appId = DataConverter.parseInt(request.getParameter("appId"), -1);
        if(appId < 0){
            appId = DataConverter.parseInt(request.getParameter("id"), -1);
            if(appId < 0){
                throw new UnknownRecordException("Unknown regprof application ID:" + appId);
            }
        }
        String saveType = request.getParameter("type");
        if (saveType.equals("piexamination")) {
            saveProfessionalInstitutionExamination(request, response, appId);
        } else if (saveType.equals("experience_examination")) {
            saveProfessionExperienceExamination(request, response);
        } else if (saveType.equals("documentexamination")) {
            saveDocumentExamination(request, response, appId);
        } else if (saveType.equals("qualificationexamination")) {
            saveQualificationExamination(request, response, appId);
        } else if (saveType.equals("status")) {
            saveManuallyChangedStatus(request, response, appId);
        } else if (saveType.equals("regulated")) {
            saveRegulatedProfessionExamination(request, response, appId);
        }
        request.setAttribute(WebKeys.ACTIVE_FORM, RegprofApplicationHandler.FORM_ID_EXAMINATION_DATA);
        request.setAttribute(WebKeys.NEXT_SCREEN, NEXT_SCREEN);
        new RegprofApplicationHandler(servletContext).handleEdit(request, response);
    }

    public void saveRegulatedProfessionExamination(HttpServletRequest request,HttpServletResponse response, Integer applicationId){
        Integer validityId = DataConverter.parseInteger(request.getParameter("chosenValidityId"), null);
        Integer examId = DataConverter.parseInteger(request.getParameter("regulatedExaminationId"), null);
        String notes = request.getParameter("regulatedExaminationNotes");        
        RegulatedProfessionExamination examination = null;        
        if (validityId == null) {
            if (examId != null) {
                getNacidDataProvider().getRegprofApplicationDataProvider().deleteRegulatedProfessionExaminationForApp(examId);
            } else {
                return;
            }
        } else {
            examination = getNacidDataProvider().getRegprofApplicationDataProvider().saveRegulatedProfessionExaminationForApp(applicationId, notes, getLoggedUser(request, response).getUserId(),
                    validityId, examId);
        }

        request.setAttribute("regulatedExaminationId", examination != null ? examination.getId(): null );
        request.setAttribute("regulatedExaminationNotes", examination != null ? examination.getNotes(): null);
    }

    private void saveManuallyChangedStatus(HttpServletRequest request, HttpServletResponse response, Integer appId) {
        Integer status = DataConverter.parseInteger(request.getParameter("status"), null);
        Integer docflowStatus = DataConverter.parseInteger(request.getParameter("docflow_status"), null);
        if (status != null) {
            RegprofApplicationDataProvider appDP = getNacidDataProvider().getRegprofApplicationDataProvider();
            RegprofApplicationAttachmentDataProvider attDP = getNacidDataProvider().getRegprofApplicationAttachmentDataProvider();
            NomenclaturesDataProvider nomDP = getNacidDataProvider().getNomenclaturesDataProvider();

            //RegprofApplicationDetails details = appDP.getRegprofApplication(appId).getApplicationDetails();
            String archiveNumber = "";
            boolean hasError = false;
//TODO:::
            /*if (docflowStatus != null && docflowStatus == ApplicationDocflowStatus.APPLICATION_OBVIOUS_FACTUAL_ERROR_DOCFLOW_STATUS_CODE) {
                List<RegprofApplicationAttachment> factualErrors = attDP.getAttachmentsForParentByType(appId, DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_APPLY);
                if (factualErrors == null || factualErrors.isEmpty()) {
                    hasError = true;
                    DocumentType docType = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_APPLY);
                    request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Няма прикачено " + docType.getName(),
                            SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                }
            } else if (docflowStatus != null && docflowStatus == ApplicationDocflowStatus.APPLICATION_DUPLICATE_DOCFLOW_STATUS_CODE) {
                List<RegprofApplicationAttachment> duplicateApplies = attDP.getAttachmentsForParentByType(appId, DocumentType.DOC_TYPE_REGPROF_DUPLICATE_APPLY);
                if (duplicateApplies == null || duplicateApplies.isEmpty()) {
                    hasError = true;
                    DocumentType docType = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_DUPLICATE_APPLY);
                    request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Няма прикачено " + docType.getName(),
                            SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                }
            } else */if (docflowStatus != null && docflowStatus == ApplicationDocflowStatus.APPLICATION_IZDADENO_DOCFLOW_STATUS_CODE) {
                List<RegprofApplicationAttachment> certificates = attDP.getAttachmentsForParentByTypes(appId, DocumentType.REGPROF_CERTIFICATES);
                if (certificates == null || certificates.isEmpty()) {
                    hasError = true;
                    request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Заявлението няма издадено удостоверение!",
                            SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                } else {
                    boolean hasDocflow = false;
                    for (RegprofApplicationAttachment certificate : certificates) {
                        if (certificate.getDocflowId() != null && !certificate.getDocflowId().isEmpty() && certificate.getDocflowDate() != null) {
                            hasDocflow = true;
                            break;
                        }
                        if (!hasDocflow) {
                            hasError = true;
                            request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Удостоверението няма генериран деловоден номер!",
                                    SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                        }
                    }
                }
            }






            if (status == ApplicationStatus.APPLICATION_OBEZSILENO_STATUS_CODE) {
                List<RegprofApplicationAttachment> invalidationRequests = attDP.getAttachmentsForParentByType(appId, DocumentType.DOC_TYPE_REGPROF_ISKANE_OBEZSILVANE);
                List<RegprofApplicationAttachment> courtDecisions = attDP.getAttachmentsForParentByType(appId, DocumentType.DOC_TYPE_REGPROF_COURT_DECISION);
                if ((invalidationRequests == null || invalidationRequests.isEmpty()) && (courtDecisions == null || courtDecisions.isEmpty())) {
                    hasError = true;
                    DocumentType invalidationRequest = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_ISKANE_OBEZSILVANE);
                    DocumentType courtDecision = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_COURT_DECISION);
                    request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Няма прикачено " + invalidationRequest.getName() + " или " + courtDecision.getName(),
                            SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                }
            } else if (status == ApplicationStatus.APPLICATION_VOLUNTARILY_TERMINATED_CODE) {
                List<RegprofApplicationAttachment> applies = attDP.getAttachmentsForParentByType(appId, DocumentType.DOC_TYPE_REGPROF_VOLUNTARILY_SUSPENSION_APPLY);
                if (applies == null || applies.isEmpty()) {
                    hasError = true;
                    DocumentType docType = nomDP.getDocumentType(DocumentType.DOC_TYPE_REGPROF_VOLUNTARILY_SUSPENSION_APPLY);
                    request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Няма прикачено " + docType.getName(),
                            SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                }
            } else if (status == ApplicationStatus.APPLICATION_REFUSED_STATUS_CODE) {
                List<RegprofApplicationAttachment> denials = attDP.getAttachmentsForParentByTypes(appId, DocumentType.REGPROF_DENIALS);
                if (denials == null || denials.isEmpty()) {
                    hasError = true;
                    request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Заявлението няма издаден отказ!",
                            SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                } else {
                    boolean hasDocflow = false;
                    for (RegprofApplicationAttachment denial : denials) {
                        if (denial.getDocflowId() != null && !denial.getDocflowId().isEmpty() && denial.getDocflowDate() != null) {
                            hasDocflow = true;
                            break;
                        }
                        if (!hasDocflow) {
                            hasError = true;
                            request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Отказът няма генериран деловоден номер!",
                                    SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                        }
                    }
                }
            }


            if (docflowStatus == ApplicationDocflowStatus.APPLICATION_IZDADENO_DOCFLOW_STATUS_CODE) {
                if (!ApplicationStatus.REGPROF_POSITIVE_STATUS_CODES.contains(status)) {
                    hasError = true;
                    request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Само удостоверено заявление може да се променя на Издадено!", SystemMessageWebModel.MESSAGE_TYPE_ERROR));
                }
            }
            if (docflowStatus == ApplicationDocflowStatus.APPLICATION_ARCHIVED_DOCFLOW_STATUS_CODE) {
                archiveNumber = DataConverter.parseString(request.getParameter("archive_number"), null);
            }

            if (!hasError) {
                appDP.saveStatusOnly(appId, status,  docflowStatus, getLoggedUser(request, response).getUserId(), archiveNumber);
                request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Статусът беше променен!",
                        SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
            }
        } else {
            request.setAttribute("applicationStatusMsg", new SystemMessageWebModel("Не е посочен статус!",
                    SystemMessageWebModel.MESSAGE_TYPE_ERROR));
        }
    }

    private void saveQualificationExamination(HttpServletRequest request, HttpServletResponse response, Integer appId) {
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();
        RegprofTrainingCourseDataProvider tcDp = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        RegprofTrainingCourseDetailsBase tcDetails = tcDp.getRegprofTrainingCourse(appId).getDetails();
        RegprofQualificationExaminationImpl qualificationExamination = new RegprofQualificationExaminationImpl();
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();

        Integer id = DataConverter.parseInteger(request.getParameter("qualificationExaminationId"), null);
        Integer userCreated = getLoggedUser(request, response).getUserId();
        Integer recognizedQualLevelId = DataConverter.parseInteger(request.getParameter("recognizedQualificationLevelId"), null);
        Integer recognizedQualDegreeId = DataConverter.parseInteger(request.getParameter("recognizedQualificationDegreeId"), null);
        Integer articleItemId = DataConverter.parseInteger(request.getParameter("articleItemId"), null);
        Integer recognizedProfessionId = DataConverter.parseInteger(request.getParameter("recognizedProfessionId"), null);    
        String recognizedProfession = request.getParameter("recognizedProfession");

        int recognizedQualificationTeacher = DataConverter.parseInt(request.getParameter("recognizedQualificationTeacher"), 0);
        Integer grade = null;
        Integer schoolType = null;
        Integer ageRange = null;
        if (recognizedQualificationTeacher == 1) {
            grade = DataConverter.parseInteger(request.getParameter("grade"), null);
            schoolType = DataConverter.parseInteger(request.getParameter("schoolType"), null);
            ageRange = DataConverter.parseInteger(request.getParameter("ageRange"), null);
        }





        //SaveProfession if not present in nomenclature
        if (recognizedProfessionId == null && recognizedProfession != "" && recognizedProfession != null) {
            recognizedProfession = DataConverter.removeRedundantWhitespaces(recognizedProfession);
            FlatNomenclature newProfession = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION, recognizedProfession);
            if (newProfession == null && recognizedProfession != null) {
                recognizedProfessionId = nomDp.saveFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_PROFESSION, 0, recognizedProfession, null, null);                
            } else if (newProfession != null) {
                recognizedProfessionId = newProfession.getId();
                recognizedProfession = newProfession.getName();
            }
        }
        qualificationExamination.setId(id);
        qualificationExamination.setRecognizedProfessionId(recognizedProfessionId);
        qualificationExamination.setRecognizedQualificationDegreeId(recognizedQualDegreeId);
        qualificationExamination.setRecognizedQualificationLevelId(recognizedQualLevelId);
        qualificationExamination.setArticleItemId(articleItemId);
        qualificationExamination.setUserCreated(userCreated);
        qualificationExamination.setTrainingCourseId(tcDetails.getId());
        qualificationExamination.setRecognizedQualificationTeacher(recognizedQualificationTeacher);
        qualificationExamination.setGrade(grade);
        qualificationExamination.setAgeRange(ageRange);
        qualificationExamination.setSchoolType(schoolType);

        appDp.saveRegprofQualificationExamination(qualificationExamination);

        request.setAttribute("qualificationExaminationSystemMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
    }

    private void saveDocumentExamination(HttpServletRequest request, HttpServletResponse response, Integer appId){        
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();

        DocumentExamination docExamination = new DocumentExaminationImpl();
        Integer userCreated = getLoggedUser(request, response).getUserId();
        docExamination.setUserCreated(userCreated);
        /*Integer id = DataConverter.parseInteger(request.getParameter("documentExaminationId"), null);
        Date examDate = DataConverter.parseDate(request.getParameter("documentExaminationDate"));
        Integer source = DataConverter.parseInteger(request.getParameter("source"), null);
        if (source == null) {
            source = DataConverter.parseInteger(request.getParameter("alternativeSource"), null);
        }*/
        Integer id = null;
        Date examDate = null;
        Integer source = null;
        Integer alternativeSource = null;

        /** screenshot upload */
        // create file upload factory and upload servlet
        DiskFileItemFactory factory = new DiskFileItemFactory();

        FileCleaningTracker pTracker = FileCleanerCleanup.getFileCleaningTracker(getServletContext());
        factory.setFileCleaningTracker(pTracker);
        ServletFileUpload upload = new ServletFileUpload(factory);

        // set file upload progress listener
        FileUploadListener listener = new FileUploadListener();

        HttpSession session = request.getSession();
        session.setAttribute(WebKeys.FILE_UPLOAD_LISTENER, listener);

        // upload servlet allows to set upload listener
        upload.setProgressListener(listener);

        Integer fileId = null;
        int fileSize = 0;
        InputStream is = null;
        List uploadedItems = null;
        String fileName = "";
        String contentType = "";
        try {
            uploadedItems = upload.parseRequest(request);
            Iterator iter = uploadedItems.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.getFieldName().equals("documentExaminationId")) {
                    id = DataConverter.parseInteger(item.getString("UTF-8"), null);
                } else if (item.getFieldName().equals("documentExaminationDate")) {
                    examDate = DataConverter.parseDate(item.getString("UTF-8"));
                } else if (item.getFieldName().equals("source")) {
                    source =  DataConverter.parseInteger(item.getString("UTF-8"), null);
                } else if (item.getFieldName().equals("alternativeSource")) {
                    alternativeSource =  DataConverter.parseInteger(item.getString("UTF-8"), null);
                } else if (item.getFieldName().equals("screenshotId")) {
                    fileId = DataConverter.parseInteger(item.getString("UTF-8"), null);
                } else if (item.getFieldName().equals("screenshot")) {
                    fileSize = (int) item.getSize();
                    if (fileSize > 0) {
                        is = item.getInputStream();
                        fileName = RegprofBaseAttachmentHandler.prepareFileName(item.getName());
                        contentType = item.getContentType();
                    }
                }
            }
            /** saving document examination */
            if (source == null) {
                source = alternativeSource;
            }
            Integer trainingCourseId = getNacidDataProvider().getRegprofApplicationDataProvider().getRegprofApplication(appId).getApplicationDetails().getTrainingCourseId();

            docExamination.setId(id);
            docExamination.setDocumentExaminationDate(examDate);
            docExamination.setSource(source);
            docExamination.setTrainingCourseId(trainingCourseId);

            docExamination = appDp.saveDocumentExamination((DocumentExaminationImpl) docExamination);
            /** end of saving document examination */
            
            if (fileSize > 0) {
                nacidDataProvider.getDocExamAttachmentDataProvider().saveAttachment(fileId != null ? fileId : 0, docExamination.getId(), null, 
                        DocumentType.DOC_TYPE_REGPROF_SCREENSHOT, 
                        null, null, null, contentType, fileName, is, fileSize, null, null, null, 0, getLoggedUser(request, response).getUserId());
            }
        } catch (Exception e) {
            throw Utils.logException(this, e);
        } finally {
            session.removeAttribute(WebKeys.FILE_UPLOAD_LISTENER);
        }
        /** end of screenshot upload */

        request.setAttribute("documentExaminationSystemMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
    }

    private void saveProfessionalInstitutionExamination(HttpServletRequest request, HttpServletResponse response, Integer appId){
        RegprofApplicationDataProvider dp = getNacidDataProvider().getRegprofApplicationDataProvider();
        Integer validityId = DataConverter.parseInteger(request.getParameter("unregulatedbgValidityId"), null);
        if(validityId == null){
            return;
        }
        Integer examinationId = DataConverter.parseInteger(request.getParameter("examinationId"), null);
        String notes = request.getParameter("examinationNotes");
        Integer userCreated = getLoggedUser(request, response).getUserId();

        dp.saveProfessionalInstitutionExaminationForApp(appId, notes, userCreated, validityId, examinationId);

        request.setAttribute("institutionValiditySystemMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
                SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
    }

    private void saveProfessionExperienceExamination(HttpServletRequest request, HttpServletResponse response) {
        Integer examinationId = DataConverter.parseInteger(request.getParameter("experience_examination_id"), -1);
        Integer experienceId = DataConverter.parseInteger(request.getParameter("profession_experience_id"), -1);
        Integer notRestricted = DataConverter.parseInteger(request.getParameter("is_unrestricted"), -1);
        Integer docRecognized = DataConverter.parseInteger(request.getParameter("document_recognized"), -1);
        Integer articleItemId = DataConverter.parseInteger(request.getParameter("articleItemExprId"), null);
        if(docRecognized == -1){
            docRecognized = DataConverter.parseInteger(request.getParameter("alternativeDocument_recognized"), -1);
        }
        Integer appId = DataConverter.parseInteger(request.getParameter("appId"), -1);
        if (examinationId < 0 || experienceId < 0 || appId < 1) {
            throw new UnknownRecordException("Unknown examinationId or experienceId: " + examinationId + "/" + experienceId);
        }
        if (docRecognized != 1) {
            docRecognized = 0;
        }
        User user = getLoggedUser(request, response);

        RegprofProfessionExperienceExaminationRecord record = new RegprofProfessionExperienceExaminationRecord(examinationId,
                experienceId, docRecognized, user.getUserId(), notRestricted, articleItemId);
        RegprofTrainingCourseDataProvider dp = getNacidDataProvider().getRegprofTrainingCourseDataProvider();
        Integer id = dp.saveProfessionExperienceExamination(record);
        if (id != null && id > 0) {
            request.setAttribute("professionExperienceSystemMessage", new SystemMessageWebModel("Данните бяха въведени в базата",
                    SystemMessageWebModel.MESSAGE_TYPE_CORRECT));
        }
        else {
            request.setAttribute("professionExperienceSystemMessage", new SystemMessageWebModel("Възникна грешка при запис на данните",
                    SystemMessageWebModel.MESSAGE_TYPE_ERROR));
        }
    }

    private static void generateProfessionalInstitutionValidityTable(NacidDataProvider nacidDataProvider, HttpServletRequest request, Integer institutionId,
            Integer qualificationId, Integer qualificationType, Integer appId) {
        List<ProfessionalInstitutionValidityImpl> instValidities = null;
        ProfessionalInstitutionExamination examination = null;
        if(institutionId != null && qualificationId != null && qualificationType != null){
            instValidities = nacidDataProvider.getProfessionalInstitutionDataProvider().
                    getProfessionalInstitutionValidities(institutionId, qualificationId, qualificationType);
            examination = nacidDataProvider.getRegprofApplicationDataProvider().
                    getProfessionalInstitutionExaminationForApp(appId);
        }

        TableFactory tableFactory = TableFactory.getInstance();
        Table table = tableFactory.createTable();      
        table.addColumnHeader(COLUMN_NAME_VALIDITY_CHECKED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
        table.addColumnHeader(COLUMN_NAME_INST_VALIDITY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_EXAMINATION_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
        table.addColumnHeader(COLUMN_NAME_VALID, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
        table.addColumnHeader(COLUMN_NAME_HAS_RIGHTS, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
        table.addColumnHeader(COLUMN_NAME_VALIDITY_NOTES, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.setUniqueColmn(COLUMN_NAME_INST_VALIDITY_ID);

        if (instValidities != null && instValidities.size() > 0) {
            for (ProfessionalInstitutionValidity v: instValidities) {
                try {
                    table.addRow(examination != null && examination.getProfessionalInstitutionValidityId().equals(v.getId()),
                            v.getId(), v.getExaminationDate(), DataConverter.parseIntegerToBoolean(v.getIsLegitimate()), 
                            DataConverter.parseIntegerToBoolean(v.getHasRightsEducate()), v.getNotes());
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, VALIDITY_LIST_PREFIX);
        tableState.setOrderCriteria(COLUMN_NAME_EXAMINATION_DATE, true);
        //Webmodel settings
        TableWebModel tableWebmodel = new TableWebModel(null);
        tableWebmodel.setColumnFormatter(COLUMN_NAME_VALIDITY_CHECKED, CellFormatter.BOOLEAN_AS_RADIO_FORMATTER);
        tableWebmodel.hideUnhideColumn(COLUMN_NAME_INST_VALIDITY_ID, true);
        if (request.getAttribute(WebKeys.OPERATION_VIEW) != Boolean.TRUE) {
            tableWebmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
            tableWebmodel.hideOperation(TableWebModel.OPERATION_NAME_VIEW);
            tableWebmodel.setGroupName("regprofapplication_examination");
        } else {
            tableWebmodel.setHasOperationsColumn(false);
        }       
        tableWebmodel.insertTableData(table, tableState);               
        request.setAttribute("rowsCount", table.getRowsCount());
        request.setAttribute(WebKeys.PROF_INSTITUTION_VALIDITIES_LIST, tableWebmodel);
    }

    private void generateRegulatedProfessionValidityTable(NacidDataProvider nacidDataProvider, HttpServletRequest request, Integer countryId, Integer appId){
        List<RegulatedProfessionValidityImpl> regValidities = null;
        RegulatedProfessionExamination examination = null;
        if(countryId != null){
            regValidities = nacidDataProvider.getRegprofApplicationDataProvider().getRegulatedProfessionValidities(countryId);
            examination = nacidDataProvider.getRegprofApplicationDataProvider().
                    getRegulatedProfessionExaminationForApp(appId);
        }

        TableFactory tableFactory = TableFactory.getInstance();
        Table table = tableFactory.createTable();      
        table.addColumnHeader(COLUMN_NAME_VALIDITY_CHECKED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
        table.addColumnHeader(COLUMN_NAME_REG_VALIDITY_ID, CellValueDef.CELL_VALUE_TYPE_INTEGER);
        table.addColumnHeader(COLUMN_NAME_EXAMINATION_DATE, CellValueDef.CELL_VALUE_TYPE_DATE);
        table.addColumnHeader(COLUMN_NAME_REGULATED, CellValueDef.CELL_VALUE_TYPE_BOOLEAN);
        table.addColumnHeader(COLUMN_NAME_PROFESSION, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.addColumnHeader(COLUMN_NAME_VALIDITY_NOTES, CellValueDef.CELL_VALUE_TYPE_STRING);
        table.setUniqueColmn(COLUMN_NAME_REG_VALIDITY_ID);

        if (regValidities != null && regValidities.size() > 0) {
            for (RegulatedProfessionValidityImpl v: regValidities) {
                try {
                    table.addRow(examination != null && examination.getRegulatedValidityId().equals(v.getId()),
                            v.getId(), v.getExaminationDate(), DataConverter.parseIntegerToBoolean(v.getIsRegulated()), 
                            v.getProfession(), v.getNotes());
                } catch (CellCreationException e) {
                    throw Utils.logException(e);
                }
            }
        }
        TableState tableState = TableStateAndFiltersUtils.createBaseTableState(request, table, VALIDITY_REG_LIST_PREFIX);
        tableState.setOrderCriteria(COLUMN_NAME_EXAMINATION_DATE, true);
        //Webmodel settings
        TableWebModel tableWebmodel = new TableWebModel(null);
        tableWebmodel.setColumnFormatter(COLUMN_NAME_VALIDITY_CHECKED, CellFormatter.BOOLEAN_AS_RADIO_FORMATTER);
        tableWebmodel.hideUnhideColumn(COLUMN_NAME_REG_VALIDITY_ID, true);
        if (request.getAttribute(WebKeys.OPERATION_VIEW) != Boolean.TRUE) {
            tableWebmodel.hideOperation(TableWebModel.OPERATION_NAME_DELETE);
            tableWebmodel.hideOperation(TableWebModel.OPERATION_NAME_VIEW);
            tableWebmodel.setGroupName("regulated_examination");
        } else {
            tableWebmodel.setHasOperationsColumn(false);
        }       
        tableWebmodel.insertTableData(table, tableState);               
        request.setAttribute("rowsCount", table.getRowsCount());
        request.setAttribute(WebKeys.REGULATED_VALIDITIES_LIST , tableWebmodel);
    }

    private void makeExperienceSettings(HttpServletRequest request, HttpServletResponse response, RegprofTrainingCourse trainingCourse){
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        RegprofTrainingCourseDataProvider trainingCourseDP = nacidDataProvider.getRegprofTrainingCourseDataProvider();
        RegprofProfessionExperienceExaminationRecord professionExperienceExamination = trainingCourseDP.getProfessionExperienceExamination(trainingCourse.getExperienceRecord().getId());
        if (professionExperienceExamination != null) {
            if (trainingCourse.getExperienceRecord() != null && trainingCourse.getDetails().getNotRestricted() == 1) {
                if (professionExperienceExamination.getNotRestricted() == 1) {
                    request.setAttribute("not_restricted", "checked=\"checked\"");
                }
                if(professionExperienceExamination.getExperienceDocumentRecognized() == 1){
                    request.setAttribute("experience_doc_recognized", "checked=\"checked\"");
                    new RegprofProfessionExperienceAttachmentHandler(servletContext).handleList(request, response);
                }
                request.setAttribute("experience_examination_id", professionExperienceExamination.getId());
                request.setAttribute("profession_experience_id", professionExperienceExamination.getRegprofProfessionExperienceId());
            } else {
                request.setAttribute("disable_experience_examination", "disabled=\"disabled\""); // tuk mai ne moje da se vleze
                request.setAttribute("disable_document_recognition", "disabled=\"disabled\"");
            }

        }
        else { // new
            request.setAttribute("experience_examination_id", 0);
            request.setAttribute("profession_experience_id", trainingCourse.getExperienceRecord().getId());
            if (trainingCourse.getExperienceRecord().getId() != 0 && trainingCourse.getDetails().getNotRestricted() == 1) {
                request.setAttribute("not_restricted", "checked=\"checked\"");
            } else {
                request.setAttribute("disable_experience_examination", "disabled=\"disabled\"");
            }
        }
        if (trainingCourse.getExperienceRecord().getId() == 0) {
            request.setAttribute("disable_experience_examination", "disabled=\"disabled\"");
            request.setAttribute("disable_document_recognition", "disabled=\"disabled\"");
        }

        List<FlatNomenclature> articles = nomDp.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, null, OrderCriteria
                .createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, false));
        RegprofArticleItem articleItem = null;
        if(professionExperienceExamination!= null && professionExperienceExamination.getArticleItemId() != null){
            articleItem = nomDp.getRegprofArticleItem(professionExperienceExamination.getArticleItemId());
        }
        ComboBoxUtils.generateNomenclaturesComboBox(articleItem!= null? articleItem.getArticleDirectiveId():null, articles, true, request, "articleDirectiveExprCombo", true);
        RegprofArticleItemsAjaxHandler.generateArticleItemCombo(request, response, nomDp, professionExperienceExamination!= null ? 
                professionExperienceExamination.getArticleItemId(): null, false, WebKeys.ARTICLE_ITEM_COMBO + "Expr");
    }

    private void makeStatusSettings(HttpServletRequest request, RegprofTrainingCourse trainingCourse, RegprofApplication appRecord){
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();


        List<ApplicationStatus> statuses = null;
        Integer statusId = appRecord.getApplicationDetails().getStatus();
        int docflowStatusId = appRecord.getApplicationDetails().getDocflowStatusId();
        if (trainingCourse.getDetails() != null && trainingCourse.getDetails().getHasExperience() == 1 &&  trainingCourse.getDetails().getNotRestricted() == 1) {
            statuses = nomDp.getApplicationStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null,
                    OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
        } else {
            List<Integer> excludingIds = new ArrayList<Integer>();
            excludingIds.add(ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE);
            excludingIds.add(ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_AND_QUALIFICATION_STATUS_CODE);
            excludingIds.add(ApplicationStatus.APPLICATION_NOT_RECOGNIZED_EXPERIENCE_STATUS_CODE);
            statuses = nomDp.getApplicationStatusesExcluding(NumgeneratorDataProvider.REGPROF_SERIES_ID, excludingIds, null,
                    OrderCriteria.createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, true), false);
            statusId = statusId == ApplicationStatus.APPLICATION_RECOGNIZED_EXPERIENCE_STATUS_CODE ? ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE : statusId;
        }
        ComboBoxUtils.generateNomenclaturesComboBox(statusId, statuses, false, request, "statusCombo", false);

        List<ApplicationDocflowStatus> docflowStatuses = nomDp.getApplicationDocflowStatuses(NumgeneratorDataProvider.REGPROF_SERIES_ID, null, null);
        ComboBoxUtils.generateNomenclaturesComboBox(docflowStatusId, docflowStatuses, false, request, "docflowStatusCombo", false);


        List<RegprofAppStatusHistoryForListRecord> statusesHistory = appDp.getRegprofAppStatusHistoryForList(appRecord.getApplicationDetails().getId());
        request.setAttribute("statusHistory", statusesHistory);

        List<RegprofAppDocflowStatusHistoryRecordExtended> docflowStatusesHistory = appDp.getAppDocflowStatusHistoryRecords(appRecord.getApplicationDetails().getId());
        request.setAttribute("docflowStatusesHistory", docflowStatusesHistory);






        RegprofApplicationDetails appDetails = appRecord.getApplicationDetails();
        if (appDetails.getArchiveNum() != null && !appDetails.getArchiveNum().isEmpty()) {
            request.setAttribute("archiveNumber", appDetails.getArchiveNum());
        } else {
            request.setAttribute("archiveNumber", ARCHIVE_NUMBER_PREFIX);
        }
        request.setAttribute("archiveNumberPrefix", ARCHIVE_NUMBER_PREFIX);
    }

    private void makeDocumentSettings(HttpServletRequest request, RegprofTrainingCourse trainingCourse, HttpServletResponse response){
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDataProvider appDp = nacidDataProvider.getRegprofApplicationDataProvider();
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        RegprofTrainingCourseDetailsBase trainingCourseDetails = trainingCourse.getDetails();

        String docSeries = null;
        String docNum = null;
        String documentRegNum = null;
        String docDate = null;
        Integer eduType = trainingCourseDetails.getEducationTypeId();
        String docType = null;

        if (eduType != null) {
            if ((eduType == EducationType.EDU_TYPE_HIGH || eduType == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL || eduType == EducationType.EDU_TYPE_SECONDARY)&& trainingCourseDetails.getDocumentType()!= null){
                FlatNomenclature docTypeNom = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, trainingCourseDetails.getDocumentType());
                docType = docTypeNom != null ? docTypeNom.getName() : "Несъществуващ вид документ";
                docNum = trainingCourseDetails.getDocumentNumber();
                docDate = trainingCourseDetails.getDocumentDate();
                documentRegNum = trainingCourseDetails.getDocumentRegNumber();
                docSeries = trainingCourseDetails.getDocumentSeries();
            } else if (eduType == EducationType.EDU_TYPE_SDK && trainingCourseDetails.getSdkDocumentType() != null) {
                FlatNomenclature docTypeNom = nomDp.getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_DOCUMENT_TYPE, trainingCourseDetails.getSdkDocumentType());
                docType = docTypeNom != null ? docTypeNom.getName() : "Несъществуващ вид документ";
                docNum = trainingCourseDetails.getSdkDocumentNumber();
                documentRegNum = trainingCourseDetails.getSdkDocumentRegNumber();
                docDate = trainingCourseDetails.getSdkDocumentDate();
                docSeries = trainingCourseDetails.getSdkDocumentSeries();
            } else {
                request.setAttribute("document_data", "noData");
            }
        }
        else {
            request.setAttribute("document_data", "noData");
        }
        request.setAttribute("doc_type_name", docType);
        DocumentExamination documentExamination = appDp.getDocumentExaminationForTrainingCourse(trainingCourseDetails.getId());
        if (docNum != null || documentRegNum != null) {
            String numDate = (docSeries != null && !docSeries.isEmpty() ? "серия: " + docSeries + ", " : "") +
                    (docNum != null && !docNum.isEmpty() ? "номер: " + docNum + ", " : "") + 
                    (documentRegNum != null && !documentRegNum.isEmpty() ? "рег. номер: " + documentRegNum + ", " : "") +
                    (docDate != null && !docDate.isEmpty() ? "от дата: "+ docDate : "");
            if (!numDate.isEmpty() && numDate.charAt(numDate.length() - 2) == ',') {
                numDate = numDate.substring(0, numDate.length() - 2);
            }
            request.setAttribute("document_num_date", numDate);
            if (documentExamination != null) {
                request.setAttribute("docExam", documentExamination);
            }
        } else {
            request.setAttribute("document_num_date", "Няма въведена информация за диплома");
            request.setAttribute("document_data", "noData");
        }
        List<DocumentExaminationSourceImpl> sources = new ArrayList<DocumentExaminationSourceImpl>();

        sources.add(new DocumentExaminationSourceImpl(SOURCE_REGISTER, "Регистър"));
        sources.add(new DocumentExaminationSourceImpl(SOURCE_COMMUNICATED_INSTITUTION, "Комуникирана институция"));
        ComboBoxUtils.generateComboBox(documentExamination != null ? documentExamination.getSource() : null, sources, request, "sourcesCombo", true, "getId", "getName");

        if (documentExamination != null && documentExamination.getSource() == SOURCE_COMMUNICATED_INSTITUTION) {
            new DocExamAttachmentHandler(servletContext).handleList(request, response);           
        } else if (documentExamination != null && documentExamination.getSource() == SOURCE_REGISTER) {
            List<RegprofApplicationAttachment> attachments = 
                    nacidDataProvider.getDocExamAttachmentDataProvider().getAttachmentsForParentByType(documentExamination.getId(), DocumentType.DOC_TYPE_REGPROF_SCREENSHOT);
            if (attachments != null && attachments.size() > 0) {
                RegprofApplicationAttachment screenshot = Utils.getListFirstElement(attachments);
                request.setAttribute("screenshotId", screenshot.getId());
                request.setAttribute("screenshotName", screenshot.getFileName());
            }
        }
    }

    private void makeQualificationSettings(HttpServletRequest request, RegprofTrainingCourse trainingCourse, HttpServletResponse response){
        NacidDataProvider nacidDataProvider = getNacidDataProvider();       
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        RegprofQualExamWebModel qualExamModel = new RegprofQualExamWebModel(nacidDataProvider, trainingCourse.getDetails());
        request.setAttribute("qualExamWebModel", qualExamModel);
        if( qualExamModel.getQualDegreesComboAttrs()!= null){
            ComboBoxUtils.generateNomenclaturesComboBox(qualExamModel.getRecognizedQualDegree(), qualExamModel.getQualDegreesComboAttrs(), true, request, "recognizedQualificationDegree", 
                    true);
        } else if(qualExamModel.getQualLevelsComboAttrs()!= null){
            ComboBoxUtils.generateNomenclaturesComboBox(qualExamModel.getRecognizedQualLevel(), qualExamModel.getQualLevelsComboAttrs(), true, request, "recognizedQualificationLevel", false);
        }
        ComboBoxUtils.generateNomenclaturesComboBox(qualExamModel.getAgeRange(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_AGE_RANGE, nomDp, false, request, "ageRangeCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(qualExamModel.getGrade(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADE, nomDp, false, request, "gradeCombo", null, true);
        ComboBoxUtils.generateNomenclaturesComboBox(qualExamModel.getSchoolType(), NomenclaturesDataProvider.FLAT_NOMENCLATURE_SCHOOL_TYPE, nomDp, false, request, "schoolTypeCombo", null, true);

        List<FlatNomenclature> articles = nomDp.getFlatNomenclatures(NomenclaturesDataProvider.FLAT_NOMENCLATURE_REGPROF_ARTICLE_DIRECTIVE, null, OrderCriteria
                .createOrderCriteria(OrderCriteria.ORDER_CRITERIA_NOMENCLATURE, NomenclatureOrderCriteria.ORDER_COLUMN_NAME, false));
        ComboBoxUtils.generateNomenclaturesComboBox(qualExamModel.getArticleId(), articles, true, request, "articleDirectiveCombo", true);
        RegprofArticleItemsAjaxHandler.generateArticleItemCombo(request, response, nomDp, qualExamModel!= null ? qualExamModel.getItemId(): null, false, WebKeys.ARTICLE_ITEM_COMBO);
    }

    private void makeInstitutionSettings(HttpServletRequest request, RegprofTrainingCourse trainingCourse, RegprofApplication appRecord){
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofTrainingCourseDetailsBase trainingCourseDetails = trainingCourse.getDetails();
        Integer eduType = trainingCourseDetails.getEducationTypeId();
        RegprofProfInstExamWebModel instExamWebModel = new RegprofProfInstExamWebModel(trainingCourseDetails, nacidDataProvider, appRecord);
        request.setAttribute("instExamWebModel", instExamWebModel);
        if (eduType != null && (eduType == EducationType.EDU_TYPE_SECONDARY || eduType == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL)) {
            generateProfessionalInstitutionValidityTable
            (nacidDataProvider, request, trainingCourseDetails.getProfInstitutionId(), instExamWebModel.getQualBgSecId(), 
                    eduType, appRecord.getApplicationDetails().getId()); 
        } else if (eduType != null && eduType == EducationType.EDU_TYPE_HIGH) {
            generateProfessionalInstitutionValidityTable
            (nacidDataProvider, request, trainingCourseDetails.getProfInstitutionId(), instExamWebModel.getQualBgHighSdkId(), 
                    eduType, appRecord.getApplicationDetails().getId());
        } else if (eduType != null && eduType == EducationType.EDU_TYPE_SDK) {
            generateProfessionalInstitutionValidityTable
            (nacidDataProvider, request, trainingCourseDetails.getSdkProfInstitutionId(), instExamWebModel.getQualBgHighSdkId(), 
                    eduType, appRecord.getApplicationDetails().getId()); 
        } else {    
            generateProfessionalInstitutionValidityTable(nacidDataProvider, request, null, null, 
                    null, appRecord.getApplicationDetails().getId());
        }
    }

    private void makeRegulatedSettings(HttpServletRequest request, Integer appId){
        NacidDataProvider nacidDataProvider = getNacidDataProvider();
        RegprofApplicationDetails details = nacidDataProvider.getRegprofApplicationDataProvider().getRegprofApplication(appId).getApplicationDetails();
        String regCountry = "Няма информация за държава";
        if(details != null && details.getApplicationCountryId()!= null){
            generateRegulatedProfessionValidityTable(nacidDataProvider, request, details.getApplicationCountryId(), appId);
            Country countryNom = nacidDataProvider.getNomenclaturesDataProvider().getCountry(details.getApplicationCountryId());
            regCountry = countryNom != null ? countryNom.getName() : "Няма запис за тази държава";
        }
        request.setAttribute("regulatedInCountry", regCountry);

        RegulatedProfessionExamination examination = nacidDataProvider.getRegprofApplicationDataProvider().getRegulatedProfessionExaminationForApp(appId);
        if(examination != null){
            request.setAttribute("regulatedExaminationId", examination.getId());
            request.setAttribute("regulatedExaminationNotes", examination.getNotes());
        }        

        if(details == null || details.getApplicationCountryId() == null){
            request.setAttribute("regData", "noData");
        }   
    }

}
//-----------------------------------------------------------------------------------------------------------------------------------