package com.nacid.bl.applications;

import com.nacid.bl.ApplicationDetailsForExpertPosition;
import com.nacid.bl.OrderCriteria;
import com.nacid.bl.docflow.DocFlowException;
import com.nacid.bl.nomenclatures.ApplicationStatus;
import com.nacid.bl.nomenclatures.DocumentType;
import com.nacid.data.applications.SimilarDiplomaRecord;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ApplicationsDataProvider {
    public int getApplicationType(int applicationId);
    public Application getApplication(int applicationId);

    public ApplicationDetailsForReport getApplicationDetailsForReport(int applicationId);

    public ApplicationDetailsForExpertPosition getApplicationDetailsForExpertPosition(int applicationId, int expertId);

    /**
     * @return samo zaqvleniqta, koito ve4e sa razperedeleni kym daden expert
     */
    public List<Application> getAssignedApplications();

    /**
     *
     * @param applicationType
     * @param onlyInProcedure - true - samo v procedura. False - vsichki, nqma znacehnie dali sa v procedura ili ne!
     * @return
     */
    public List<Application> getApplications(int applicationType, boolean onlyInProcedure);


    /**
     * @param onlyInProcedure - vry6ta samo zapisite, koito as v procedura!
     * @return
     */
    public List<ApplicationForList> getApplicationsForList(int applicationType, Integer applicationStatus, boolean onlyInProcedure);


    public List<ApplicationForList> getApplicationsByCommission(int calendarId);
    /**
     *
     * @param statuses
     * @param onlySelected
     * @return zaqvleniqta, koito shte se pokazvat v handleList na ApplicationsHandler
     */
    //public List<? extends ApplicationForList> getApplicationsForList(List<Integer> statuses, boolean onlySelected);

    /**
     * @param applicantId
     * @return zaqvleniqta za daden applicant
     */
    public List<Application> getApplicationsByApplicant(int applicantId);

    /**
     * @param applicationIds - spisyk ot applicationIds
     * @return
     */
    public List<Application> getApplications(List<Integer> applicationIds, OrderCriteria orderCriteria);

    public Person getPerson(int personId);

    public PersonDocument getPersonDocument(int personId);

    /**
     * @param civilIdType   == 0 - ne u4astva vyv filtriraneto
     * @param civilId == null - ne u4astva vyv filtriraneto
     * @param isPartOfCivilId - true - tyrsi po chast ot civilId-to, false - tyrsi po tochno syvpadenie
     * @return
     */
    public List<Person> getPersons(int civilIdType, String civilId, boolean isPartOfCivilId);

    /**
     * tozi metod se izpolzva v parse-vaneto na APRP
     *
     * @param civilId - celiqt personalen identifikator
     * @return Person s tochniq identifikator ako ima takyv
     */
    public Person getPersonByCivilId(String civilId);

    /**
     * ako applicationNumber/applicationDate sa null, to se generirat applicatioNumber/Date za rudiApplication. Za statut/avtentichnost/preporyka/ ne se generirat nomera, a napravo se prehvurlqt generiranite ot front office-a!
     */
    public int saveApplication(
            int id,
            String applicationNumber,
            Date applicationDate,
            Integer applicantId,
            Integer applicantCompanyId,
            Integer representativeId,
            int trainingCourseId,
            String email,
            int officialEmailComm,
            int homeCountryId,
            String homeCity,
            String homePostCode,
            String homeAddressDetails,
            boolean homeIsBg,
            String bgCity,
            String bgPostCode,
            String bgAddressDetails,
            Integer bgAddressOwner,
            int createdByUserId,
            Date timeOfCreation,
            int applicationStatusId,
            Integer companyId,
            int differentDiplomaNames,
            String homePhone,
            String bgPhone,
            Integer reprCountryId,
            String reprCity,
            String reprPcode,
            String reprAddressDetail,
            String reprPhone,
            String summary,
            String archiveNumber,
            Boolean personalDataUsage,
            Boolean dataAuthentic,
            Boolean representativeAuthorized,
            String notes,
            String submittedDocs,
            String applicantInfo, int userUpdated, Integer responsibleUser, int applicationDocflowStatusId, String representativeType, Short typePayment,
            Integer deliveryType, Boolean declaration, String courierNameAddress, String outgoingNumber, String internalNumber, Boolean isExpress, int applicantType,
            int applicationType,
            Integer documentReceiveMethodId,
            Integer applicantPersonalIdDocumentType);
    public void deleteApplicationKinds(int applicationId);
    public void addApplicationKind(int applicationId, int entryNumSeriesId, BigDecimal price, String entryNum, Integer finalStatusHistoryId);

    /**
     * @param applicationId
     * @param applicationStatusId
     * @param archiveNum          - ako e null, ne se promenq archiveNumber-a na zaqvlenieto!
     * @param legalReasonId       Tozi method se vika pri promqna na statusa prez application/status
     */
    public void updateApplicationStatus(int applicationId, int applicationStatusId, int docflowStatusId, String archiveNum, Integer legalReasonId, String submittedDocs, int userUpdated);

    /**
     * tozi method se vika pri promqna na motivite prez - komisiq - kalendar - obrabotka - promqna na motivi
     *
     * @param applicationId
     * @param applicationStatusId - nov status
     * @param applicantInfo       - informaciq za zaqvitelq
     * @param motives             - motivi
     * @param legalReasonId       - pravno osnovanie
     * @param sessionId           - sesiq na komisiqta na koqto sa promeneni status/motivi/applicantInfo na zaqvlenie
     */
    public void updateMotives(int applicationId, int applicationStatusId, String applicantInfo, String motives, Integer legalReasonId, int sessionId, int userUpdated);

    public int savePerson(int id, String firstName, String surName, String lastName, String civilId, int civilIdType, Integer birthCountry, String birthCity, Date birthDate, Integer citizenshipId);

    public int savePersonDocument(Integer id, int personId, Date dateOfIssue, String issuedBy, String number);

    public List<Person> getPersons();

    public boolean deletePerson(int personId);

    public void deleteApplicationRecognitionPurpose(int applicationId);

    /**
     * predi da po4nat da se vyvejdat novi ApplicationRecognitionPurposeRecords, trqbva da se iztriqt starite ot bazata!!!!
     */
    public void addApplicationRecognitionPurposeRecord(int applicationId, Integer recognitionPurposeId, String notes);

    /**
     * iztriva applicationExperts za daden applicationId. Predi da se vyvede nov applicationExpert trqbva da se zatriqt starite zapisi!
     *
     * @param applicationId
     */
    public void deleteApplicationExperts(int applicationId);

    public void addApplicationExpert(int applicationId, int expertId, String notes, int finished, String courseContent, Integer expertPosition, Integer legalReasonId, List<Integer> specialityIds, Integer qualificationId, Integer eduLevelId, String previousBoardDecisions, String similarBulgarianPrograms);

    public List<AppStatusHistory> getAppStatusHistory(int applId);

    public List<AppDocflowStatusHistory> getAppDocflowStatusHistory(int applicationId);

    /**
     * @param applicationId
     * @return posledniq legalReasonId za daden applicationId
     */
    public Integer getLastLegalReasonId(int applicationId);

    public List<Application> getApplicationsForExpert(int expertId);

    public boolean hasExpertAccessToApplication(int applicationId, int expertId);

    /**
     * shte vry6ta dali dadeno zaqvlenie ot vytre6nata baza e podadeno elektronno - v momenta elektronno podadeno zaqvlenie e tova, koeto ima settnato application_id v ext_appliction
     */
    public boolean isElectronicallyApplied(int applicationId);

    public void setExpertProcessStatus(int applicationId, int expertId, int pocessStat);

    public ApplicationExpert getApplicationExpert(int applicationId, int expertId);

    /**
     * @param finalStatus - {@link ApplicationStatus#APPLICATION_IZDADENO_STATUS_CODE}, {@link ApplicationStatus#APPLICATION_REFUSED_STATUS_CODE},{@link ApplicationStatus#APPLICATION_OBEZSILENO_STATUS_CODE}. pri pyrvite 2 statusa se tyrsi v application.final_status. pri obezsileno se tyrsi dali nqkoga v appStatusHistory ima status obezsileno
     * @return
     */
    public List<? extends ApplicationForPublicRegister> getApplicationsByFinalStatus(int finalStatus);


    public Integer getApplicationIdForExtReport(String docflowNumber, Date docflowDate, String personalId);

    public Application getApplicationByMail(String email);

    /**
     * proverqva dali moje da se generira certificateNumber za dadeno zaqvlenie. Ako ne moje da generira, hvyrlq exception
     *
     * @param applicationId
     * @throws CertificateNumberGenerationException - 1. Ako status-a  na zaqvlenieto e razlichen ot {@link ApplicationStatus#APPLICATION_PRIZNATO_STATUS_CODE}<br /> 2. ako nqma komisiq, razglejdala dadenoto zaqvlenie. Syob6tenieto koeto da se izvede na potrebitelq moje da se vzeme ot {@link CertificateNumberGenerationException#getMessage()}
     */
//    public void checkCertificateNumberGeneration(int applicationId) throws CertificateNumberGenerationException;

    public String generateCertificateNumber(int applicationId) throws CertificateNumberGenerationException;
    //public String getCertificateNumber(int applicationId);

    /**
     * @param certNumber
     * @param personalId
     * @return applicationId po zadadeni certNumber i personalId
     */
    public Integer getApplicationIdForExtReport(String certNumber, String personalId);

    public int getApplicationIdByDiplomaExaminationId(int diplomaExaminationId);

    //syzdava attachment ot tip dublikat, kato pravi stariq attachment unishtojeno!
    public void duplicateApplicationCertificate(int applicationId, int userChanged);

    //suzdava attachment ot tip ochevidna fakticheska greshka, kato pravi stariq attachment unishtojeno
    public void duplucateApplicationCertificateBecauseOfFactualError(int applicationId, int userChanged);

    /**
     * tyrsi applications, koito imat diplomi s ednakvi dolnite parametri - eduLevelId/diplomaYear/(universityCountryIds ili universityCountryNames)/(civilId ili firstName/secondName/lastName)/ Ako nqkoi ot parametrite e null, to toj ne ucastva vyv filtriraneto!
     * @return
     */
    public List<SimilarDiploma> getSimilarDiplomas(String civilId, String firstName, List<Integer> universityCountryIds, List<String> universityCountryNames, Integer eduLevelId, Integer diplomaYear, Integer skipApplicationId);

    /***
     * vry6ta poluchatelq na udostoverenieto
     * @param applicationId
     * @return
     */
    public DocumentRecipient getDocumentRecipient(int applicationId);

    void saveDocumentRecipient(int applicationId, String name, int countryId, String city, String district, String postCode, String address, String mobilePhone);

    void deleteDocumentRecipient(int applicationId);
}
