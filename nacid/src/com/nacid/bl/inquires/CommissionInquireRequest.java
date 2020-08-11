package com.nacid.bl.inquires;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Georgi
 * Date: 14.4.2020 г.
 * Time: 0:00
 */
@Getter
public class CommissionInquireRequest {
    /**
     * jointDegreesFlag - 0 - bez syvmestni stepeni, 1 - samo syvmestni stepeni; null - nqma znachenie
     * startSessionId - nachalen nomer na zasedanie na komisiqta. Ako e null, ne u4astva vyv filtriraneto
     * endSessionId - kraen nomer na zasedanie na komisiqta. Ako e null, ne u4astva vyv filtriraneto
     * fromDate - data ma provejdane na zasedanie ot koqto zapo4va tyrseneto. Ako null, ne u4astva vyv filtriraneto
     * toDate - data ma provejdane na zasedanie do koqto svyrshva tyrseneto. Ako null, ne u4astva vyv filtriraneto
     * applicationStatuses - List ot {@link ApplicationStatusFromCommissionInquire}. Tozi klas sydyrja vryzkata applicationStatus null/AND/NOT/ joinStatus, t.e. zaqvleniq koito sa sys status1 "i"/ "ili ne"/ status2.
     * applicantCountryIds - spisyk s id-ta na dyrjavi na zaqvitel.
     * universityCountryIds - spisyk s dyrjavi na universiteti - Ako e izbrana dadena dyrjava se tyrsqt vsi4ki universiteti ot tazi dyrjava. Rezultatite obedinqvat universityCountryIds i universityIds, t.e. ako e izbrano dyrjava bylgaria, i university=moskovski universitet, shte se vyrne imformaciq za universitetite v bulgaria + moskovski universitet!!! Ako e null ili ima size == 0, ne u4astva vyv filtriraneto, no ako e null i universities.size() == 0, tozi method vry6ta null!!!
     * universityIds - spisyk s id-ta na universiteti. Rezultatite obedinqvat universityCountryIds i universityIds, t.e. ako e izbrano dyrjava bylgaria, i university=moskovski universitet, shte se vyrne imformaciq za universitetite v bulgaria + moskovski universitet!!!  Ako ima size == 0 i countryIds == null, vry6ta null!
     * diplomaSpecialityIds - spisyk ot id-na na specialnosti po diploma. Ako e izbrana pove4e ot 1 specialnost, se pravi obedinenie mejdu vsi4ki izbrani specialnosti! Ako e null - ne u4astva vyv filtriraneto. Ako size() == 0, vry6ta null
     * diplomaQualificationIds - spisyk ot kvalifikacii po diploma. Ako e izbrana pove4e ot 1 kvalifikaciq, se pravi obedinenie mejdu vsi4ki izbrani kvalifikacii. Ako e null ili ima size == 0, ne u4astva vyv filtriraneto
     * diplomaEducationLevelIds - spisyk ot education levels(Obrazovatelno-Kvalifikacionni Stepeni) po diploma. Ako e izbran pove4e ot edna OKS, rezultatite vkliu4vat obedinienie mejdu vsi4ki. Ako e null ili ima size == 0, ne u4astva vyv filtriraneto
     * recognizedSpecialityIds - priznati specialities
     * recognizedQualificationIds - priazniti qualifications
     * recognizedEducationLevelIds - priznati education level
     */
    protected List<ApplicationTypeAndEntryNumSeries> applicationTypeEntryNumSeries;
    protected Integer startSessionId;
    protected Integer endSessionId;
    protected Date dateFrom;
    protected Date dateTo;
    protected Date diplomaDateFrom;
    protected Date diplomaDateTo;
    protected List<ApplicationStatusFromCommissionInquire> applicationStatuses = new ArrayList<ApplicationStatusFromCommissionInquire>();
    //protected Map<Integer, List<Integer>> applicationStatusIds;
    //protected Integer appStatusId;
    //protected List<Integer> applicationStatusIds;
    //protected List<Integer> legalReasonIds;
    protected List<Integer> applicantCountryIds;
    protected List<Integer> diplomaSpecialityIds;
    protected List<Integer> diplomaQualificationIds;
    protected List<Integer> diplomaEducationLevelIds;
    protected List<Integer> recognizedSpecialityIds;
    protected List<Integer> recognizedQualificationIds;
    protected List<Integer> recognizedEducationLevelIds;


    protected List<Integer> universityCountryIds;
    protected List<Integer> universityIds;
    protected Date appDateFrom;
    protected Date appDateTo;
    protected Integer jointDegreeFlag;
}
