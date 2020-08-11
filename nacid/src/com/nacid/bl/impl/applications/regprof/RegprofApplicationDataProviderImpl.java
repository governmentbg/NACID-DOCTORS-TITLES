
package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.*;
import com.nacid.bl.applications.regprof.*;
import com.nacid.bl.docflow.DocFlowException;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.impl.numgenerator.NumgeneratorDataProviderImpl;
import com.nacid.bl.impl.regprof.RegulatedProfessionValidityImpl;
import com.nacid.bl.impl.users.regprof.ResponsibleUserImpl;
import com.nacid.bl.nomenclatures.*;
import com.nacid.bl.nomenclatures.regprof.ServiceType;
import com.nacid.bl.numgenerator.NumgeneratorDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitutionDataProvider;
import com.nacid.bl.regprof.ProfessionalInstitutionValidity;
import com.nacid.bl.regprof.RegulatedProfessionValidity;
import com.nacid.bl.users.UsersDataProvider;
import com.nacid.bl.users.regprof.ResponsibleUser;
import com.nacid.data.DataConverter;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.applications.PersonRecord;
import com.nacid.data.regprof.applications.*;
import com.nacid.data.users.ResponsibleUserRecord;
import com.nacid.db.applications.regprof.RegprofApplicationDB;
import com.nacid.db.regprof.RegprofApplicationAttachmentDB;
import com.nacid.db.utils.StandAloneDataSource;
import com.nacid.report.TemplateGenerator;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
//RayaWritten-----------------------------------------------------------------------------
public class RegprofApplicationDataProviderImpl implements RegprofApplicationDataProvider {

    private NacidDataProviderImpl nacidDataProvider;
    private RegprofApplicationDB db;
    private RegprofTrainingCourseDataProvider tcdp;
    
    private static final String REGPROF_APPLICATION_TYPE_NAME = "regprofApplicationType";

    public RegprofApplicationDataProviderImpl(NacidDataProviderImpl nacidDataProvider){
        this.nacidDataProvider = nacidDataProvider;
        this.db = new RegprofApplicationDB(nacidDataProvider.getDataSource());
        this.tcdp = new RegprofTrainingCourseDataProviderImpl(nacidDataProvider);
    }

    @Override
    public RegprofApplicationImpl saveRegprofApplicationRecord(RegprofApplicationImpl info, int userUpdated) {
        int oldApplicationStatusId;
        int oldDocflowStatusId;
        try {
            if (info.getApplicationDetails().getId() == null || info.getApplicationDetails().getId() == 0) {
                try {
                    prepareApplicationNumberAndDate(info);

                    oldApplicationStatusId = Integer.MIN_VALUE;
                    oldDocflowStatusId = Integer.MIN_VALUE;


                    if (info.getApplicationDetails().getStatus() == null) {
                        info.getApplicationDetails().setStatus(ApplicationStatus.APPLICATION_PODADENO_STATUS_CODE);
                    }
                    if (info.getApplicationDetails().getDocflowStatusId() == 0) {
                        info.getApplicationDetails().setDocflowStatusId(ApplicationDocflowStatus.APPLICATION_VPROCEDURA_DOCFLOW_STATUS_CODE);
                    }
                } catch (DocFlowException e) {
                    throw Utils.logException(e);
                }
                if (info.getApplicant().getId() == null) {
                    info.setApplicant(db.insertRecord(info.getApplicant()));
                    info.getApplicationDetails().setApplicantId(info.getApplicant().getId());
                }
                if ((info.getRepresentative().getId() == null || info.getRepresentative().getId() == 0) && !StringUtils.isEmpty(info.getRepresentative().getCivilId()) &&
                        !StringUtils.isEmpty(info.getRepresentative().getFName())){
                    info.setRepresentative(db.insertRecord(info.getRepresentative()));
                    info.getApplicationDetails().setRepresentativeId((info.getRepresentative().getId()));
                }
                saveApplicantDocumentsIfAny(info);

                if (info.getTrCourseDocumentPersonDetails().getId() == null) {
                    Integer trainingCourseDetailsId = tcdp.insertEmptyTCDetailsRecord();
                    info.getTrCourseDocumentPersonDetails().setId(trainingCourseDetailsId);
                    if(info.getApplicationDetails().getNamesDontMatch() != 0){
                        tcdp.updateRegprofTrainingCourseDetails(info.getTrCourseDocumentPersonDetails(), "document_fname", "document_sname", "document_lname", "document_civil_id_type", 
                                "document_civil_id");
                    }
                    info.getApplicationDetails().setTrainingCourseId(trainingCourseDetailsId);
                }
                info.setApplicationDetails(db.insertRecord(info.getApplicationDetails()));
                
            } else {
                if (info.getApplicant().getId() == null || info.getApplicant().getId() == 0) {
                    info.setApplicant(db.insertRecord(info.getApplicant()));
                    info.getApplicationDetails().setApplicantId(info.getApplicant().getId());
                }
                if ((info.getRepresentative().getId() == null || info.getRepresentative().getId() == 0) && !StringUtils.isEmpty(info.getRepresentative().getCivilId()) &&
                        !StringUtils.isEmpty(info.getRepresentative().getFName())){
                    info.setRepresentative(db.insertRecord(info.getRepresentative()));
                    info.getApplicationDetails().setRepresentativeId((info.getRepresentative().getId()));
                }
                saveApplicantDocumentsIfAny(info);

                if (info.getApplicationDetails().getNamesDontMatch() != 0) {
                    tcdp.updateRegprofTrainingCourseDetails(info.getTrCourseDocumentPersonDetails(), "document_fname", "document_sname", "document_lname", "document_civil_id_type", "document_civil_id");
                } else {
                    RegprofTrainingCourseDetailsImpl emptyTCDetails = new RegprofTrainingCourseDetailsImpl();
                    emptyTCDetails.setId(info.getTrCourseDocumentPersonDetails().getId());
                    info.setTrCourseDocumentPersonDetails(emptyTCDetails);
                    tcdp.updateRegprofTrainingCourseDetails(emptyTCDetails, "document_fname", "document_sname", "document_lname", "document_civil_id_type", "document_civil_id");
                }

                RegprofApplicationDetailsImpl oldRecord = new RegprofApplicationDetailsImpl();
                oldRecord = db.selectRecord(oldRecord, info.getApplicationDetails().getId());

                info.getApplicationDetails().setAppNum(oldRecord.getAppNum());
                info.getApplicationDetails().setAppDate(oldRecord.getAppDate());
                db.updateRecord(info.getApplicationDetails());   
                oldApplicationStatusId = info.getApplicationDetails().getStatus();
                oldDocflowStatusId = oldRecord.getDocflowStatusId();

            }

            DocumentReceiveMethod documentReceiveMethod = info.getApplicationDetails().getDocumentReceiveMethodId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getDocumentReceiveMethod(info.getApplicationDetails().getDocumentReceiveMethodId());
            if (documentReceiveMethod == null || !documentReceiveMethod.hasDocumentRecipient()) {
                db.deleteDocumentRecipient(info.getApplicationDetails().getId());
                info.setDocumentRecipient(null);
            } else if (documentReceiveMethod != null){
                info.getDocumentRecipient().setApplicationId(info.getApplicationDetails().getId());
                db.saveDocumentRecipient(info.getDocumentRecipient());
            }

            addStatusHistoryRecordIfStatusChanged(info.getApplicationDetails().getId(), oldApplicationStatusId, info.getApplicationDetails().getStatus(), null, null, userUpdated);

            addDoclfowStatusHistoryRecordIfStatusChanged(info.getApplicationDetails().getId(), oldDocflowStatusId, info.getApplicationDetails().getDocflowStatusId(), userUpdated);

            db.deleteRecords(ResponsibleUserRecord.class, " regprof_application_id = ?", info.getId());
            if(info.getResponsibleUsers() != null && info.getResponsibleUsers().size() > 0){
                for(ResponsibleUser ru: info.getResponsibleUsers()){
                    db.insertRecord(new ResponsibleUserRecord(ru.getId(), info.getApplicationDetails().getId(), ru.getUserId()));                   
                }
            }
            return info;
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    private void saveApplicantDocumentsIfAny(RegprofApplicationImpl info) throws SQLException {
        if (info.getApplicantDocuments() != null && (info.getApplicantDocuments().getId() == null || info.getApplicantDocuments().getId() == 0)) {
            if ((!StringUtils.isEmpty(info.getApplicantDocuments().getIssuedBy()) && !StringUtils.isEmpty(info.getApplicantDocuments().getNumber()) && info.getApplicantDocuments().getDateOfIssue() != null)) {
                db.setOldDocumentsInactive(info.getApplicant().getId());
                info.getApplicantDocuments().setPersonId(info.getApplicant().getId());
                info.getApplicantDocuments().setActive(1);
                info.setApplicantDocuments(db.insertRecord(info.getApplicantDocuments()));
            }

            info.getApplicationDetails().setApplicantDocumentsId(info.getApplicantDocuments().getId());
        }
    }
    
    public RegprofApplicationDetails saveRegprofApplicationDetails(RegprofApplicationDetails details) {
        try {
            return db.insertRecord(details);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    private void prepareApplicationNumberAndDate(RegprofApplicationImpl record) throws DocFlowException {
        
        NumgeneratorDataProviderImpl numgeneratorDataProvider = nacidDataProvider.getNumgeneratorDataProvider();
        String nextNum = numgeneratorDataProvider.getNextNumber(NumgeneratorDataProvider.REGPROF_SERIES_ID, NumgeneratorDataProvider.SUBMISSION_TYPE_DESK);//TODO:Zasega vinagi podavam "na gishe"
        record.getApplicationDetails().setAppNum(nextNum);
        record.getApplicationDetails().setAppDate(numgeneratorDataProvider.getWorkingDate());

    }

    /**
     * dobavq zapis v appStatusHistory ako status-a na zaqvlenieto e promenen!
     * 
     * @param oldApplicationStatusId - star status
     * @param newApplicationStatusId - nov status
     * @param newLegalReasonId - nov legalReasonId
     * @return
     * @throws SQLException
     */
    private void addStatusHistoryRecordIfStatusChanged(int appId, int oldApplicationStatusId, int newApplicationStatusId, Integer newLegalReasonId, Integer sessionId, int userAssigned) throws SQLException { 
        List<RegprofAppStatusHistoryImpl> appStatuses = db.selectRecords(RegprofAppStatusHistoryImpl.class, " application_id = ? ", appId);
        RegprofAppStatusHistoryImpl lastAppStatus = appStatuses.size() == 0 ? null : appStatuses.get(0);

        //Zapis v historyto se zapisva samo ako e promenen status-a ili e promenen legalReason-a
        if( oldApplicationStatusId != newApplicationStatusId || lastAppStatus == null ){
            RegprofAppStatusHistoryImpl histRec = new RegprofAppStatusHistoryImpl(0, appId, newApplicationStatusId, new java.sql.Date(System.currentTimeMillis()), newLegalReasonId, sessionId, userAssigned);
            db.insertRecord(histRec);


            // update-va final_status_history_id v application obekta pri neobhodimost (t.e. statusa e smenen + noviq status e praven)
            ApplicationStatus status = nacidDataProvider.getNomenclaturesDataProvider().getApplicationStatus(NumgeneratorDataProvider.REGPROF_SERIES_ID, histRec.getStatusId());
            if (status.isLegal()) {
                db.updateApplicationFinalStatusHistoryRecordId(appId, histRec.getId());
            }
        }
    }

    private void addDoclfowStatusHistoryRecordIfStatusChanged(int applicationId, int oldDocflowStatusId, int newDocflowStatusId, int userAssigned) throws SQLException {
        List<RegprofAppDocflowStatusHistoryRecordExtended> appStatuses = db.getAppDocflowStatusHistoryRecords(applicationId);
        RegprofAppDocflowStatusHistoryRecord lastAppStatus = appStatuses.size() == 0 ? null : appStatuses.get(0);
        //Zapis v historyto se zapisva samo ako e promenen status-a
        if(oldDocflowStatusId != newDocflowStatusId || lastAppStatus == null) {
            RegprofAppDocflowStatusHistoryRecord histRec = new RegprofAppDocflowStatusHistoryRecord(0, applicationId, newDocflowStatusId, new java.sql.Date(System.currentTimeMillis()), userAssigned);
            db.insertRecord(histRec);
        }
    }

    public List<RegprofAppDocflowStatusHistoryRecordExtended> getAppDocflowStatusHistoryRecords(int applicationId) {
        try {
            return db.getAppDocflowStatusHistoryRecords(applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public RegprofApplication getRegprofApplication(int applicationId) {
        try {
            UsersDataProvider usersDP = nacidDataProvider.getUsersDataProvider();
            RegprofApplication record = new RegprofApplicationImpl();
            RegprofApplicationDetails details = db.getRegprofApplicationDetails(applicationId);
            record.setApplicationDetails((RegprofApplicationDetailsImpl) details);
            PersonRecord applicant = db.getPerson(details.getApplicantId());
            record.setApplicant(applicant);
            record.setDocumentRecipient(db.getDocumentRecipient(applicationId));
            PersonRecord representative = null;
            if (details.getRepresentativeId() != null) {           
                representative = db.getPerson(details.getRepresentativeId());
            }
            RegprofTrainingCourseDetailsImpl trainingCourseDetails = tcdp.getRegprofTrainingCourseDetails(details.getTrainingCourseId());
            record.setRepresentative(representative);
            record.setTrCourseDocumentPersonDetails(trainingCourseDetails);
            record.setApplicantDocuments(details.getApplicantDocumentsId() == null ? null : db.getPersonDocumentById(details.getApplicantDocumentsId()));
            List<ResponsibleUserRecord> responsibleUsers = db.selectRecords(ResponsibleUserRecord.class, "regprof_application_id = ? ", applicationId);
            List<ResponsibleUser> responsibleUsersImpl = null;
            if (responsibleUsers!= null && responsibleUsers.size() > 0) {
                responsibleUsersImpl = new ArrayList<ResponsibleUser>();
                for(ResponsibleUserRecord rec : responsibleUsers){
                    responsibleUsersImpl.add(new ResponsibleUserImpl(rec, usersDP));
                }
            }
            record.setResponsibleUsers(responsibleUsersImpl);
            return record;

        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public String generateCertificateNumber(int applicationId) throws CertificateNumberGenerationException {
        try {
            RegprofApplication app = getRegprofApplication(applicationId);
            if (app == null) {
                throw new RuntimeException("No application with applicationId = " + applicationId);
            }

            String certificateNumber = "";
            //Ako zaqvlenieto ima ve4e izdaden certificateNumber, t.e. ima zapis v tablicata cert_number_to_attached_docs, togava stava vypros za pre-izdavane na nov certificateNumber, koito se polu4ava ot stariq, kato mu se dobavi xx-yy-1/dneshna data;xx-yy-2/dneshna data i t.n.
            List<RegprofCertificateNumberToAttachedDocRecord> certificateNumberToAttachedDocRecords = db.getCertificateNumberToAttachedDocRecords(applicationId, null);
            if (certificateNumberToAttachedDocRecords != null && certificateNumberToAttachedDocRecords.size() > 0) {
                String oldCertificateNumber = certificateNumberToAttachedDocRecords.get(0).getCertificateNumber();
                String oldNumber = oldCertificateNumber.split("/")[0];
                String[] parts = oldNumber.split("-");
                Integer currentCertificateNumber = null;
                if (parts.length == 3) {
                    currentCertificateNumber = 1;
                } else {
                    if (parts.length == 4) {
                        currentCertificateNumber = DataConverter.parseInteger(parts[3], null);
                    }
                    if (currentCertificateNumber == null) {
                        throw new RuntimeException("Unexpected old certificateNumber format. Expected xx-yy-nn/dd.mm.yyyy. OldCertificateNumber=" + oldCertificateNumber);
                    }
                    currentCertificateNumber++;
                }
                certificateNumber = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + currentCertificateNumber + "/" + DataConverter.formatDate(new Date());
            } else {
                if (!ApplicationStatus.REGPROF_POSITIVE_STATUS_CODES.contains(app.getApplicationStatusId())) {
                    throw new CertificateNumberGenerationException("Не може да се генерира номер на удостоверение, тъй като статусът на заявлението не е подходящ");
                }
                //DocFlowNumber docFlowNumber = new DocFlowNumber(app.getDocFlowNumber());
                //certificateNumber += docFlowNumber.getPrepNo() + "-";

                /*CommissionCalendarDataProviderImpl commissionCalendarDataProvider = nacidDataProvider.getCommissionCalendarDataProvider();
                CommissionCalendar commissionCalendar = commissionCalendarDataProvider.getLastCommissionExaminedApplication(app.getId());
                if (commissionCalendar == null) {
                    throw new CertificateNumberGenerationException("Не може да се генерира номер на удостоверение, тъй като заявлението не е разгледано от комисия.");
                }
                certificateNumber += commissionCalendar.getSessionNumber();*/
                
                //certificateNumber += "/" + DataConverter.formatDate(commissionCalendar.getDateAndTime());
                
                //certificateNumber += docFlowNumber.getDocflowId() + "/" + DataConverter.formatDate(docFlowNumber.getDocflowDate());
                certificateNumber += app.getApplicationNumber() + "/" + DataConverter.formatDate(new Date()); //TODO: dali da e tova
            }

            return certificateNumber;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<? extends RegprofApplicationDetailsForList> getRegprofApplicationDetailsForList(boolean onlyInProcedure) {
        return db.getRegprofApplicationDetailsForList(onlyInProcedure);
    }



    @Override
    public List<PersonDocument> getPersonsDocuments(List<Person> persons) {
        List<PersonDocument> documents = new ArrayList<PersonDocument>();
        if(persons == null){
            return null;
        } else {
            try {
                for(Person per: persons){
                    List <PersonDocumentRecord> records = db.getDocumentRecordsByPersonId(per.getId());
                    if(records.size()>0){
                        documents.add(records.get(records.size()-1));
                    } else {
                        documents.add(null);
                    }
                }
                return documents;
            } catch(SQLException e){
                throw Utils.logException(e);
            }
        }
    }
    
    public List<PersonDocumentRecord> getDocumentRecordsByPersonId(int personId) {
        try {
            return db.getDocumentRecordsByPersonId(personId);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    //Getting the corresponding professional institution examination
    @Override
    public ProfessionalInstitutionExamination getProfessionalInstitutionExaminationForApp(Integer appId) {
        try {
            List<ProfessionalInstitutionExaminationImpl> records = db.selectRecords(ProfessionalInstitutionExaminationImpl.class,
                    " regprof_application_id = ? ", appId);
            if (records.size() > 0) {
                return records.get(0);
            } else {
                return null;
            }
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public ProfessionalInstitutionExamination saveProfessionalInstitutionExaminationForApp(Integer appId, String notes,
            Integer userCreated, Integer validityId, Integer id) {
        try{
            ProfessionalInstitutionDataProvider pidp = nacidDataProvider.getProfessionalInstitutionDataProvider();
            ProfessionalInstitutionValidity validation = pidp.getProfessionalInstitutionValidityById(validityId);
            int legitimate = 0;
            if (validation.getHasRightsEducate() == 1 && validation.getIsLegitimate() == 1) {
                legitimate = 1;
            }
            ProfessionalInstitutionExamination examination = new ProfessionalInstitutionExaminationImpl(id, validation.getId(), appId, legitimate,
                    validation.getExaminationDate(), notes, userCreated);
            if(id != null){
                db.updateRecord(examination);
            } else {
                examination = db.insertRecord(examination);
            }
            return examination;
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void deleteProfessionalInstitutionExaminationForApp(Integer id) {
        try {
            db.deleteRecord(ProfessionalInstitutionExaminationImpl.class, id);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    public RegprofAppStatusHistoryForListRecord getFinalStatusHistoryRecord(int appId) {
        try {
            return db.getFinalStatusHistoryRecord(appId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    //getting the status information (history) about the current app
    @Override
    public List<RegprofAppStatusHistoryForListRecord> getRegprofAppStatusHistoryForList(Integer appId) {
        try {
            return db.getStatusHistoryForList(appId);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    @Override
    public void saveStatusOnly(int applicationId, Integer newStatus, Integer docflowStatus, int userAssigned, String archiveNumber) {

        try {
            RegprofApplicationDetails details = db.getRegprofApplicationDetails(applicationId);
            int oldStatus = details.getStatus();
            int oldDocflowStatus = details.getDocflowStatusId();

            addStatusHistoryRecordIfStatusChanged(details.getId(), oldStatus, newStatus, null, null, userAssigned);

            addDoclfowStatusHistoryRecordIfStatusChanged(details.getId(), oldDocflowStatus, docflowStatus, userAssigned);


            details.setStatus(newStatus);
            List<String> columns = new ArrayList<String>();
            columns.add("status");
            details.setDocflowStatusId(docflowStatus);
            columns.add("docflow_status_id");


            if (archiveNumber != null && !archiveNumber.isEmpty()) {
                columns.add("archive_num");
                details.setArchiveNum(archiveNumber);
            }
            db.updateRecord(details, columns.toArray(new String[0]));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }
    
    @Override
    public void saveIMIOnly(RegprofApplicationDetails details, String imiCorrespondence){
        try {
            if(imiCorrespondence != null){
                details.setImiCorrespondence(imiCorrespondence);
                db.updateRecord(details, "imi_correspondence");
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public DocumentExamination getDocumentExaminationForTrainingCourse(Integer trainingCourseId) {
        List<DocumentExaminationImpl> examinations = null;
        try {
            examinations = db.selectRecords(DocumentExaminationImpl.class, " training_course_id = ? ", trainingCourseId);
            if (examinations != null && examinations.size() > 0) {
                return examinations.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public DocumentExamination saveDocumentExamination(DocumentExaminationImpl record) {
        try {
            if (record.getId() == null || record.getId() == 0) {
                record = db.insertRecord(record);
            } else {
                db.updateRecord(record);
                record = db.selectRecord(record, record.getId());
            }
            return record;
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    @Override
    public void deleteDocumentExamination(Integer id) {
        try {
            db.deleteRecord(DocumentExaminationImpl.class, id);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    @Override
    public RegprofQualificationExamination getRegprofQualificationExaminationForTrainingCourse(Integer trainingCourseId) {
        List<RegprofQualificationExaminationImpl> examinations = null;
        try {
            examinations = db.selectRecords(RegprofQualificationExaminationImpl.class, " training_course_id = ? ", trainingCourseId);
            if (examinations != null && examinations.size() > 0) {
                return examinations.get(0);
            } else {
                return null;
            }
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void saveRegprofQualificationExamination(RegprofQualificationExamination record) {
        try {
            if (record.getId() == null || record.getId() == 0) {
                db.insertRecord(record);
            } else {
                db.updateRecord(record);
            }
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public void deleteRegprofQualificationExamination(Integer id) {
        try {
            db.deleteRecord(RegprofQualificationExaminationImpl.class, id);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    public List<RegulatedProfessionValidityImpl> getRegulatedProfessionValidities(Integer countryId) {
        List<RegulatedProfessionValidityImpl> validities = null;
        try {
            /*if(educationTypeId != null && (educationTypeId == EducationType.EDU_TYPE_HIGH || educationTypeId == EducationType.EDU_TYPE_SDK)){
                validities = db.selectRecords(RegulatedProfessionValidityImpl.class, "country_id = ? AND qualification_high_sdk_id =? ", countryId, qualificationId);
            } else if(educationTypeId != null && (educationTypeId == EducationType.EDU_TYPE_SECONDARY || educationTypeId == EducationType.EDU_TYPE_SECONDARY_PROFESSIONAL)){
                validities = db.selectRecords(RegulatedProfessionValidityImpl.class, "country_id = ? AND qualification_sec_id =? ", countryId, qualificationId);
            } else if(educationTypeId == null && professionId != null){
                validities = db.selectRecords(RegulatedProfessionValidityImpl.class, "country_id = ?  AND profession_experience_id =? ", countryId, professionId);
            }*/
            validities = db.selectRecords(RegulatedProfessionValidityImpl.class, " country_id = ? ", countryId);
            return validities;
        } catch (SQLException e){
            throw Utils.logException(e);
        }        
    }

    public RegulatedProfessionValidityImpl getRegulatedProfessionValidityById(Integer id) {
        try {
            return db.selectRecord(new RegulatedProfessionValidityImpl(), id);
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    public RegulatedProfessionValidityImpl saveRegulatedProfessionValidity(RegulatedProfessionValidityImpl record) {
        try {
            if (record.getId() == null || record.getId() == 0) {
                return db.insertRecord(record);
            } else {
                db.updateRecord(record);
                return db.selectRecord(new RegulatedProfessionValidityImpl(), record.getId());
            }
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    public RegulatedProfessionExamination getRegulatedProfessionExaminationForApp(Integer appId) {
        List<RegulatedProfessionExaminationImpl> examinations = null;
        try {
            examinations = db.selectRecords(RegulatedProfessionExaminationImpl.class, " regprof_application_id = ?", appId);
            if (examinations!= null && examinations.size()>0) {
                return examinations.get(0);
            } else {
                return null;
            }
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    public RegulatedProfessionExamination saveRegulatedProfessionExaminationForApp(Integer appId, String notes,
            Integer userCreated, Integer validityId, Integer id){
        try {
            RegulatedProfessionExamination examination = null;
            RegulatedProfessionValidity validity = getRegulatedProfessionValidityById(validityId);
            if (validity == null) {
                examination = new RegulatedProfessionExaminationImpl();
                examination.setId(id);
                examination.setRegprofApplicationId(appId);
                examination.setUserCreated(userCreated);
            } else {
                examination = new RegulatedProfessionExaminationImpl(id, validityId, appId, validity.getIsRegulated(), validity.getExaminationDate(), notes, userCreated); 
            }
            if (examination.getId() == null || examination.getId() == 0) {
                return db.insertRecord(examination);
            } else {
                db.updateRecord(examination);
                return db.selectRecord(new RegulatedProfessionExaminationImpl(), examination.getId());
            }
        } catch(SQLException e) {
            throw Utils.logException(e);
        }
    }

    public void deleteRegulatedProfessionExaminationForApp(Integer id) {
        try {
            db.deleteRecord(RegulatedProfessionExaminationImpl.class, id);
        } catch(SQLException e){
            throw Utils.logException(e);
        }
    }

    public RegprofApplicationDetailsForReport getRegprofApplicationDetailsForReport(Integer applicationId) {
        try {
            RegprofApplication application = getRegprofApplication(applicationId);
            if (application == null) {
                return null;
            }
            return new RegprofApplicationDetailsForReportImpl(nacidDataProvider, application);
        } catch(Exception e) {
            throw Utils.logException(e);
        }
    }

    public List<? extends RegprofApplicationForPublicRegister> getRegprofApplicationsByFinalStatuses(List<Integer> finalStatusIds) {
        try {
            return db.getRegprofApplicationRecordsForPublicRegisterByFinalStatuses(finalStatusIds);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public Integer getRegprofApplicationIdForExtReport(String docflowNumber, Date docflowDate, String personalId){
        try {
            return db.getApplicationIdForExtReport(docflowNumber,
                    Utils.getSqlDate(docflowDate), personalId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public Integer getRegprofApplicationIdForExtReport(String certNumber, String personalId) {
        try {
            return db.getRegprofApplicationIdForExtReport(certNumber, personalId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public Date calculateApplicationEndDate(Date appDate, Integer servTypeId){
        NomenclaturesDataProvider nomDp = nacidDataProvider.getNomenclaturesDataProvider();
        if(servTypeId == null || servTypeId == 0){
            servTypeId = ServiceType.SERVICE_TYPE_STANDARD;
        }
        ServiceType serviceType = nomDp.getServiceType(servTypeId);
        if(serviceType != null){
            if (appDate == null) {
                appDate = new Date();
            }
            Integer days = serviceType.getExecutionDays();
            Calendar cal = new GregorianCalendar();
            cal.setTime(appDate);
            Utils.clearTimeFields(cal);
            cal.add(Calendar.DATE, days);
            return cal.getTime();
        } else {
            return null;
        }
    }

    public void saveCertificateNumber(int applicationId, Integer attachmentId, String certificateNumber, UUID uuid)
            throws CertificateNumberGenerationException {
        try {
            if (StringUtils.isEmpty(certificateNumber) || uuid == null) {
                throw new RuntimeException("Certificate number and uuid should not be null");
            }

            db.invalidateOldCertificateNumbers(applicationId, AttachmentDataProvider.CERTIFICATE_STATUS_UNISHTOJENO);

            db.insertRecord(new RegprofCertificateNumberToAttachedDocRecord(0, certificateNumber, attachmentId, applicationId, 0, uuid));
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public RegprofCertificateNumberToAttachedDocRecord getCertificateNumber(int applicationId, Integer invalidated) {
        try {
            List<RegprofCertificateNumberToAttachedDocRecord> certificateNumbers = db.getCertificateNumberToAttachedDocRecords(applicationId, invalidated);
            if (certificateNumbers != null && !certificateNumbers.isEmpty()) {
                return Utils.getListLastElement(certificateNumbers);
            }
        } catch (SQLException e) {
            Utils.logException(e);
        }
        return null;
    }
    
    public void invalidateOldCertificateNumbers(int applicationId) {
        try {
            db.invalidateOldCertificateNumbers(applicationId, AttachmentDataProvider.CERTIFICATE_STATUS_OBEZSILENO);
        } catch (SQLException e) {
            Utils.logException(e);
        }
    }
    
    public void saveResponsibleUser(ResponsibleUserRecord record) {
        try {
            db.insertRecord(record);
        } catch (SQLException e) {
            Utils.logException(e);
        }
    }






    public void duplicateApplicationCertificate(int applicationId, int userChanged) {
        duplicateApplicationCertificate(applicationId, DocumentType.DOC_TYPE_REGPROF_DUPLICATE_CERTIFICATE, userChanged);
    }
    public void duplicateApplicationCertificateBecauseOfFactualError(int applicationId, int userChanged) {
        duplicateApplicationCertificate(applicationId, DocumentType.DOC_TYPE_REGPROF_FACTUAL_ERROR_CERTIFICATE, userChanged);
    }

    public void duplicateApplicationCertificate(int applicationId, int newCertificateDocumentType, int userChanged) {




        try {
            RegprofApplicationDetailsImpl applicationRecord = db.selectRecord(RegprofApplicationDetailsImpl.class, "id = ?", applicationId);

            RegprofApplicationAttachmentDB attachmentDB = new RegprofApplicationAttachmentDB(nacidDataProvider.getDataSource(), RegprofApplicationAttachmentDB.REGPROF_ATTACHMENT_TYPE.REGPROF_APPLICATION);

            List<RegprofCertificateNumberToAttachedDocRecord> certNumbersToAttachedDocs = db.getCertificateNumberToAttachedDocRecords(applicationId, 0);
            for (RegprofCertificateNumberToAttachedDocRecord certNumbersToAttachedDoc : certNumbersToAttachedDocs) {
                if (certNumbersToAttachedDoc.getAttachedDocId() != null) {
                    attachmentDB.updateDocumentType(certNumbersToAttachedDoc.getAttachedDocId(), DocumentType.DOC_TYPE_REGPROF_UNISHTOJENO);
                }
            }

            List<Integer> suggestionIds = attachmentDB.getAttachmentIdsByApplicationAndDocumentTypes(applicationId, DocumentType.REGPROF_CERTIFICATE_SUGGESTIONS);

            if (suggestionIds == null || suggestionIds.size() != 1) {
                throw new RuntimeException("Unknown suggestions count....");//TODO:Dali vinagi ima samo edno predlojenie????
            }


            RegprofApplicationAttachmentRecord suggestionAttacmentRecord = attachmentDB.loadRecord(suggestionIds.get(0), false, false);

            int certificateDocumentTypeId = DocumentType.REGPROF_SUGGESTIONS_TO_CERTIFICATES.get(suggestionAttacmentRecord.getDocTypeId());

            NomenclaturesDataProvider nomenclaturesDataProvider = nacidDataProvider.getNomenclaturesDataProvider();
            DocumentType certificateDocumentType = nomenclaturesDataProvider.getDocumentType(certificateDocumentTypeId);

            String certNumber = generateCertificateNumber(applicationId);
            UUID uuid = UUID.randomUUID();
            InputStream is = TemplateGenerator.generateRegprofCertificate(nacidDataProvider, applicationId, certificateDocumentType, certNumber, uuid);

            String fileName = applicationRecord.getAppNum() + "_" + certificateDocumentType.getDocumentTemplate() + "_" + DataConverter.formatDate(new Date()) + ".doc";
            RegprofApplicationAttachmentRecord rec = new RegprofApplicationAttachmentRecord(0, applicationId, null, newCertificateDocumentType, "application/msword", fileName, is, 0, null, null, null, null, null);
            RegprofApplicationAttachmentRecord certificate = attachmentDB.saveRecord(rec, is.available(), 0, userChanged);
            saveCertificateNumber(applicationId, certificate.getId(), certNumber, uuid);


        } catch (SQLException e) {
            throw Utils.logException(e);
        } catch (IOException e) {
            throw Utils.logException(e);
        }

    }

    public RegprofApplicationDB getDb() {
        return db;
    }

    public static void main(String[] args) {
        NacidDataProvider nacidDataProvider = new NacidDataProviderImpl(new StandAloneDataSource());
        RegprofApplicationDataProviderImpl dp = (RegprofApplicationDataProviderImpl) nacidDataProvider.getRegprofApplicationDataProvider();
//        dp.duplicateApplicationCertificate(321, 1);
        dp.generateCertificateNumber(321);
    }
}

//----------------------------------------------------------------------------------------