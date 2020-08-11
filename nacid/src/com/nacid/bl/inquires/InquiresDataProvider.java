package com.nacid.bl.inquires;

import com.nacid.bl.applications.ApplicationDetailsForReport;
import com.nacid.bl.nomenclatures.SessionStatus;
import com.nacid.data.inquire.ApplicationForInquireRecord;
import com.nacid.data.inquire.BGAcademicRecognitionForReportRecord;

import java.util.List;

public interface InquiresDataProvider {

    /**
     * @return Applications s izbranite kriterii. Tyrsi samo v zasedaniq sys status = {@link SessionStatus#SESSION_STATUS_PROVEDENO}
     */
    public List<ApplicationForInquireRecord> getApplicationsForCommissionInquire(CommissionInquireRequest request);

    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForCommissionInquire(CommissionInquireRequest request);

    /**
     * @return Applications s izbranite kriterii.
     */
    public List<ApplicationForInquireRecord> getApplicationsForCommonInquire(CommonInquireRequest request);


    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForCommonInquire(CommonInquireRequest request);

    /**
     * @return Applications za spravkata "spravki za zaqvitel"
     */
    public List<ApplicationForInquireRecord> getApplicationsForApplicantInquire(ApplicantInquireRequest request);

    /**
     * @return ApplicationDetailsForReport za spravkata "spravki za zaqvitel"
     */
    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForApplicantInquire(ApplicantInquireRequest request);

    /**
     * @return applications za spravkata "zapitvane"
     */
    public List<ApplicationForInquireRecord> getApplicationsForInquiryInquire(InquiryInquireRequest request);

    /**
     * @return applicationDetailsForReport za "printirai" v spravkata "zapitvane"
     */
    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForInquiryInquire(InquiryInquireRequest request);

    public List<ApplicationForInquireRecord> getApplicationsForEmployeeInquire(EmployeeInquireRequest request);

    public List<ApplicationDetailsForReport> getApplicationDetailsForReportForEmployeeInquire(EmployeeInquireRequest request);


    public List<BGAcademicRecognitionForReportRecord> getBgAcademicRecognitionForReportRecords(List<String> ownerNames, List<String> citizenship, List<String> university,
                                                                                               List<String> universityCountry, List<String> diplomaSpeciality, List<String> educationLevel, String protocolNumber,
                                                                                               String denialProtocolNumber, List<String> recognizedSpeciliaty,
                                                                                               List<Integer> recognizedUniversityIds, String outputNumber, String inputNumber, List<Integer> recognitionStatusIds);


    public List<ExpertInquireResult> getExpertInquireResult(ExpertInquireRequest request);
}
