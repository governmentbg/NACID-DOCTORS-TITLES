package com.nacid.bl.inquires;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 9.4.2020 г.
 * Time: 15:37
 */
@Getter
public class CommonInquireRequest {

    /**
     * 	 startSessionId - nachalen nomer na zasedanie na komisiqta. Ako e null, ne u4astva vyv filtriraneto
     * 	 endSessionId - kraen nomer na zasedanie na komisiqta. Ako e null, ne u4astva vyv filtriraneto
     * 	 commissionDateFrom - data ma provejdane na zasedanie ot koqto zapo4va tyrseneto. Ako null, ne u4astva vyv filtriraneto
     * 	 commissionDateTo - data ma provejdane na zasedanie do koqto svyrshva tyrseneto. Ako null, ne u4astva vyv filtriraneto
     * 	 commissionApplicationStatuses - List ot {@link ApplicationStatusFromCommissionInquire}. Tozi klas sydyrja vryzkata applicationStatus null/AND/NOT/ joinStatus, t.e. zaqvleniq koito sa sys status1 "i"/ "ili ne"/ status2.
     * 	 diplomaDateFrom - nachanla data na diplomata
     * 	 diplomaDateTo - krajna data na diplomata
     * 	 * 
     * 	 jointDegreesFlag - 0 - bez syvmestni stepeni, 1 - samo syvmestni stepeni; null - nqma znachenie
     * 	 eSubmited - dali trqbva da vry6ta samo elektronno podadeni dokumenti - true - elektronno podadeni, false - vsi4ki
     * 	 eSigned - dali da vry6ta samo elektronno podpisanite zaqvleniq. Null - bez zna4enie, true - samo elektronno podpisani, false - samo elektronno ne-podpisani
     * 	 applicationStatusIds - Map - key - applicationStatusId, value - List<Integer> - legalReasonIds
     * 	 applicantCountryIds - spisyk s id-ta na dyrjavi na zaqvitel. 
     * 	 universityCountryIds - spisyk s dyrjavi na universiteti - Ako e izbrana dadena dyrjava se tyrsqt vsi4ki universiteti ot tazi dyrjava. Rezultatite obedinqvat universityCountryIds i universityIds, t.e. ako e izbrano dyrjava bylgaria, i university=moskovski universitet, shte se vyrne imformaciq za universitetite v bulgaria + moskovski universitet!!! Ako e null ili ima size == 0, ne u4astva vyv filtriraneto, no ako e null i universities.size() == 0, tozi method vry6ta null!!!
     * 	 universityIds - spisyk s id-ta na universiteti. Rezultatite obedinqvat universityCountryIds i universityIds, t.e. ako e izbrano dyrjava bylgaria, i university=moskovski universitet, shte se vyrne imformaciq za universitetite v bulgaria + moskovski universitet!!!  Ako ima size == 0 i countryIds == null, vry6ta null!
     * 	 diplomaSpecialityIds - spisyk ot id-na na specialnosti po diploma. Ako e izbrana pove4e ot 1 specialnost, se pravi obedinenie mejdu vsi4ki izbrani specialnosti! Ako e null - ne u4astva vyv filtriraneto. Ako size() == 0, vry6ta null
     * 	 diplomaQualificationIds - spisyk ot kvalifikacii po diploma. Ako e izbrana pove4e ot 1 kvalifikaciq, se pravi obedinenie mejdu vsi4ki izbrani kvalifikacii. Ako e null ili ima size == 0, ne u4astva vyv filtriraneto
     * 	 diplomaEducationLevelIds - spisyk ot education levels(Obrazovatelno-Kvalifikacionni Stepeni) po diploma. Ako e izbran pove4e ot edna OKS, rezultatite vkliu4vat obedinienie mejdu vsi4ki. Ako e null ili ima size == 0, ne u4astva vyv filtriraneto
     * 	 recognizedSpecialityIds - priznati specialities
     * 	 recognizedQualificationIds - priazniti qualifications
     * 	 recognizedEducationLevelIds - priznati education level
     */

    /**
     * samo zaqvleniq minali prez komisiq
     */
    protected List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNumSeries;
    protected boolean onlyCommissionApplications;
    protected boolean eSubmited;
    protected Boolean eSigned;
    protected Date appDateFrom;
    protected Date appDateTo;
    protected Date commissionDateFrom;
    protected Date commissionDateTo;
    protected Integer startSessionId;
    protected Integer endSessionId;
    protected Date diplomaDateFrom;
    protected Date diplomaDateTo;
    /**
     * status ot komisiqta!
     */
    protected List<ApplicationStatusFromCommissionInquire> commissionApplicationStatuses = new ArrayList<>();


    //protected Integer appStatusId;
    //protected Map<Integer, List<Integer>> applicationStatusIds;
    /**
     * spiosyk sys statusi (status ot/do, delovoden status, praven status)
     */
    protected List<ApplicationStatusFromCommonInquire> applicationStatuses;
    //protected List<Integer> applicationStatusIds;
    //protected List<Integer> legalReasonIds;
    protected List<Integer> applicantCountryIds;
    protected List<Integer> diplomaSpecialityIds;
    protected List<Integer> diplomaOriginalSpecialityIds;
    protected List<Integer> diplomaQualificationIds;
    protected List<Integer> diplomaOriginalQualificationIds;
    protected List<Integer> diplomaOriginalEducationLevelIds;
    protected List<Integer> diplomaEducationLevelIds;
    protected List<Integer> recognizedSpecialityIds;
    protected List<Integer> recognizedQualificationIds;
    protected List<Integer> recognizedEducationLevelIds;
    protected List<Integer> attachmentDocumentTypeIds;

    protected List<Integer> universityCountryIds;
    protected List<Integer> universityIds;

    protected List<Integer> trainingInstitutionCountryIds;
    protected List<Integer> trainingInstitutionIds;

    protected List<Integer> userCreatedIds;
    protected List<Integer> responsibleUserIds;
    protected Integer jointDegreeFlag;
    protected boolean onlyUniversitiesWithPublicRegisters;
    protected List<Integer> documentReceiveMethods;
    protected CompanyApplicantRequest companyApplicantRequest;
}
