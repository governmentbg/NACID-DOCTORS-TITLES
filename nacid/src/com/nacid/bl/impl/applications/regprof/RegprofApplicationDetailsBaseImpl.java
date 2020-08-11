package com.nacid.bl.impl.applications.regprof;


public class RegprofApplicationDetailsBaseImpl {
    protected Integer id;
    protected Integer applicantId;
    protected Integer representativeId;
    protected String applicantPhone;
    protected String applicantCity;
    protected String applicantAddrDetails;
    protected Integer applicantCountryId;
    protected Integer applicantDocumentsId;
    protected Integer status;
    protected int personalDataUsage;
    protected Integer trainingCourseId;
    protected Integer applicationCountryId;
    protected Integer serviceTypeId;
    protected int namesDontMatch;
    protected int apostilleApplication;
    protected Integer documentReceiveMethodId;
    protected Integer applicantPersonalIdDocumentType;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getApplicantId() {
        return applicantId;
    }
    public void setApplicantId(Integer applicantId) {
        this.applicantId = applicantId;
    }
    
    public String getApplicantPhone() {
        return applicantPhone;
    }
    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }
    public String getApplicantCity() {
        return applicantCity;
    }
    public void setApplicantCity(String applicantCity) {
        this.applicantCity = applicantCity;
    }
    public String getApplicantAddrDetails() {
        return applicantAddrDetails;
    }
    public void setApplicantAddrDetails(String applicantAddrDetails) {
        this.applicantAddrDetails = applicantAddrDetails;
    }
    public Integer getApplicantCountryId() {
        return applicantCountryId;
    }
    public void setApplicantCountryId(Integer applicantCountryId) {
        this.applicantCountryId = applicantCountryId;
    }
    public Integer getApplicantDocumentsId() {
        return applicantDocumentsId;
    }
    public void setApplicantDocumentsId(Integer applicantDocumentsId) {
        this.applicantDocumentsId = applicantDocumentsId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public int getPersonalDataUsage() {
        return personalDataUsage;
    }
    public void setPersonalDataUsage(int personalDataUsage) {
        this.personalDataUsage = personalDataUsage;
    }
    public Integer getTrainingCourseId() {
        return trainingCourseId;
    }
    public void setTrainingCourseId(Integer trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }
    public Integer getApplicationCountryId() {
        return applicationCountryId;
    }
    public void setApplicationCountryId(Integer applicationCountryId) {
        this.applicationCountryId = applicationCountryId;
    }
    public Integer getServiceTypeId() {
        return serviceTypeId;
    }
    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
    public int getNamesDontMatch() {
        return namesDontMatch;
    }
    public void setNamesDontMatch(int namesDontMatch) {
        this.namesDontMatch = namesDontMatch;
    }
    public void setApostilleApplication(int apostilleApplication) {
        this.apostilleApplication = apostilleApplication;
    }
    public int getApostilleApplication() {
        return apostilleApplication;
    }

    public Integer getDocumentReceiveMethodId() {
        return documentReceiveMethodId;
    }

    public void setDocumentReceiveMethodId(Integer documentReceiveMethodId) {
        this.documentReceiveMethodId = documentReceiveMethodId;
    }

    public Integer getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(Integer representativeId) {
        this.representativeId = representativeId;
    }

    public Integer getApplicantPersonalIdDocumentType() {
        return applicantPersonalIdDocumentType;
    }

    public void setApplicantPersonalIdDocumentType(Integer applicantPersonalIdDocumentType) {
        this.applicantPersonalIdDocumentType = applicantPersonalIdDocumentType;
    }
}
