package com.nacid.bl.applications.regprof;

import com.nacid.bl.applications.Person;
import com.nacid.bl.applications.PersonDocument;
import com.nacid.bl.impl.applications.regprof.DocumentExaminationImpl;
import com.nacid.bl.impl.applications.regprof.RegprofApplicationImpl;
import com.nacid.bl.impl.regprof.RegulatedProfessionValidityImpl;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.regprof.applications.RegprofAppDocflowStatusHistoryRecordExtended;
import com.nacid.data.regprof.applications.RegprofAppStatusHistoryForListRecord;
import com.nacid.data.regprof.applications.RegprofCertificateNumberToAttachedDocRecord;
import com.nacid.data.users.ResponsibleUserRecord;

import java.util.Date;
import java.util.List;


public interface RegprofApplicationDataProvider {

    public RegprofApplication saveRegprofApplicationRecord(RegprofApplicationImpl info, int userUpdated);
    public RegprofApplication getRegprofApplication(int applicationId);
    public String generateCertificateNumber(int applicationId);
    public List<? extends RegprofApplicationDetailsForList> getRegprofApplicationDetailsForList(boolean onlyInProcedure);
    public List<PersonDocument> getPersonsDocuments(List<Person> persons);
    public List<PersonDocumentRecord> getDocumentRecordsByPersonId(int personId);
    public ProfessionalInstitutionExamination getProfessionalInstitutionExaminationForApp(Integer appId);
    public List<RegprofAppStatusHistoryForListRecord> getRegprofAppStatusHistoryForList(Integer appId);
    public List<RegprofAppDocflowStatusHistoryRecordExtended> getAppDocflowStatusHistoryRecords(int applicationId);
    public RegprofAppStatusHistoryForListRecord getFinalStatusHistoryRecord(int appId);


    public ProfessionalInstitutionExamination saveProfessionalInstitutionExaminationForApp(Integer appId, String notes,
            Integer userCreated, Integer validityId, Integer id);
    public void saveStatusOnly(int applicationId, Integer newStatus, Integer docflowStatus, int userAssigned, String archiveNumber);
    public void saveIMIOnly(RegprofApplicationDetails details, String imiCorrespondence);
    
    public DocumentExamination getDocumentExaminationForTrainingCourse(Integer trainingCourseId);   
    public DocumentExamination saveDocumentExamination(DocumentExaminationImpl record);
    public RegprofQualificationExamination getRegprofQualificationExaminationForTrainingCourse(Integer trainingCourseId);  
    public void saveRegprofQualificationExamination(RegprofQualificationExamination record);
    public List<RegulatedProfessionValidityImpl> getRegulatedProfessionValidities(Integer countryId);
    public RegulatedProfessionValidityImpl saveRegulatedProfessionValidity(RegulatedProfessionValidityImpl record);
    public RegulatedProfessionValidityImpl getRegulatedProfessionValidityById(Integer id);
    public RegulatedProfessionExamination getRegulatedProfessionExaminationForApp(Integer appId);
    public RegulatedProfessionExamination saveRegulatedProfessionExaminationForApp(Integer appId, String notes,
            Integer userCreated, Integer validityId, Integer id);
    public void deleteProfessionalInstitutionExaminationForApp(Integer id);
    public void deleteDocumentExamination(Integer id);
    public void deleteRegprofQualificationExamination(Integer id);
    public void deleteRegulatedProfessionExaminationForApp(Integer id);
    public RegprofApplicationDetailsForReport getRegprofApplicationDetailsForReport(Integer appId);


    public List<? extends RegprofApplicationForPublicRegister> getRegprofApplicationsByFinalStatuses(List<Integer> finalStatusesId);


    public Integer getRegprofApplicationIdForExtReport(String docflowNumber, Date docflowDate, String personalId);
    public Integer getRegprofApplicationIdForExtReport(String certNumber, String personalId);
    /**
     * calculates end date for given appDate and serviceTypeId!
     * @param appDate
     * @param servTypeId
     * @return
     */
    public Date calculateApplicationEndDate(Date appDate, Integer servTypeId);
    public RegprofCertificateNumberToAttachedDocRecord getCertificateNumber(int applicationId, Integer invalidated);
    public void invalidateOldCertificateNumbers(int applicationId);
    public RegprofApplicationDetails saveRegprofApplicationDetails(RegprofApplicationDetails details);
    public void saveResponsibleUser(ResponsibleUserRecord record);

    public void duplicateApplicationCertificate(int applicationId, int userChanged);
    public void duplicateApplicationCertificateBecauseOfFactualError(int applicationId, int userChanged);
}


