package com.nacid.bl.impl.applications.regprof;

import java.util.Date;
import java.util.List;

public class RegProfApplicationForInquireImpl {
    private Integer id;
    private String appNumber;
    private Date appDate;
    private String docflowNumber;
    private String applicantDiplomaNames;
    private String applicantNames;
    private String civilId;
    private String citizenshipName;
    private int electronicallyApplied;
    private int esigned;
    private String serviceType;
    private String applicationCountry;
    private String status;
    private String docflowStatus;
    private String certificateQualification;
    private String professionalInstitutionName;
    private String professionalInstitutionFormerName;
    private String sdkProfessionalInstitutionName;
    private String sdkProfessionalInstitutionFormerName;
    private String documentDate;
    private String educationType;
    private String highProfessionalQualification;
    private String sdkProfessionalQualification;
    private String secondaryProfessionalQualification;
    private List<String> higherSpecialities;
    private List<String> sdkSpecialities;
    private List<String> secondarySpecialities;
    private String professionExperienceName;
    private Integer professionExperienceYears;
    private Integer professionExperienceMonths;
    private Integer professionExperienceDays;
    private String recognizedQualificationDegree;
    private String recognizedEducationLevel;
    private String recognizedProfession;
    private String recognizedArticleItem;
    private String recognizedArticleDirective;
    private String imiCorrespondence;

    private Integer educationTypeId;
    private Integer hasExperience;
    private Integer hasEducation;

    private List<String> professionExperienceDocuments;



    public String getDocflowNumber() {
        return docflowNumber;
    }

    public void setDocflowNumber(String docflowNumber) {
        this.docflowNumber = docflowNumber;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getAppNumber() {
        return appNumber;
    }
    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }
    public Date getAppDate() {
        return appDate;
    }
    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }
    public String getApplicantDiplomaNames() {
        return applicantDiplomaNames;
    }
    public void setApplicantDiplomaNames(String applicantDiplomaNames) {
        this.applicantDiplomaNames = applicantDiplomaNames;
    }
    public String getCivilId() {
        return civilId;
    }
    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCertificateQualification() {
        return certificateQualification;
    }
    public void setCertificateQualification(String certificateQualification) {
        this.certificateQualification = certificateQualification;
    }

    public String getApplicantNames() {
        return applicantNames;
    }

    public void setApplicantNames(String applicantNames) {
        this.applicantNames = applicantNames;
    }

    public String getCitizenshipName() {
        return citizenshipName;
    }

    public void setCitizenshipName(String citizenshipName) {
        this.citizenshipName = citizenshipName;
    }

    public int getElectronicallyApplied() {
        return electronicallyApplied;
    }

    public void setElectronicallyApplied(int electronicallyApplied) {
        this.electronicallyApplied = electronicallyApplied;
    }

    public int getEsigned() {
        return esigned;
    }

    public void setEsigned(int esigned) {
        this.esigned = esigned;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getApplicationCountry() {
        return applicationCountry;
    }

    public void setApplicationCountry(String applicationCountry) {
        this.applicationCountry = applicationCountry;
    }

    public String getProfessionalInstitutionName() {
        return professionalInstitutionName;
    }

    public void setProfessionalInstitutionName(String professionalInstitutionName) {
        this.professionalInstitutionName = professionalInstitutionName;
    }

    public String getProfessionalInstitutionFormerName() {
        return professionalInstitutionFormerName;
    }

    public void setProfessionalInstitutionFormerName(String professionalInstitutionFormerName) {
        this.professionalInstitutionFormerName = professionalInstitutionFormerName;
    }

    public String getSdkProfessionalInstitutionName() {
        return sdkProfessionalInstitutionName;
    }

    public void setSdkProfessionalInstitutionName(String sdkProfessionalInstitutionName) {
        this.sdkProfessionalInstitutionName = sdkProfessionalInstitutionName;
    }

    public String getSdkProfessionalInstitutionFormerName() {
        return sdkProfessionalInstitutionFormerName;
    }

    public void setSdkProfessionalInstitutionFormerName(String sdkProfessionalInstitutionFormerName) {
        this.sdkProfessionalInstitutionFormerName = sdkProfessionalInstitutionFormerName;
    }

    public String getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    public String getHighProfessionalQualification() {
        return highProfessionalQualification;
    }

    public void setHighProfessionalQualification(String highProfessionalQualification) {
        this.highProfessionalQualification = highProfessionalQualification;
    }

    public String getSdkProfessionalQualification() {
        return sdkProfessionalQualification;
    }

    public void setSdkProfessionalQualification(String sdkProfessionalQualification) {
        this.sdkProfessionalQualification = sdkProfessionalQualification;
    }

    public String getSecondaryProfessionalQualification() {
        return secondaryProfessionalQualification;
    }

    public void setSecondaryProfessionalQualification(String secondaryProfessionalQualification) {
        this.secondaryProfessionalQualification = secondaryProfessionalQualification;
    }

    public List<String> getHigherSpecialities() {
        return higherSpecialities;
    }

    public void setHigherSpecialities(List<String> higherSpecialities) {
        this.higherSpecialities = higherSpecialities;
    }

    public List<String> getSdkSpecialities() {
        return sdkSpecialities;
    }

    public void setSdkSpecialities(List<String> sdkSpecialities) {
        this.sdkSpecialities = sdkSpecialities;
    }

    public List<String> getSecondarySpecialities() {
        return secondarySpecialities;
    }

    public void setSecondarySpecialities(List<String> secondarySpecialities) {
        this.secondarySpecialities = secondarySpecialities;
    }

    public String getProfessionExperienceName() {
        return professionExperienceName;
    }

    public void setProfessionExperienceName(String professionExperienceName) {
        this.professionExperienceName = professionExperienceName;
    }

    public Integer getProfessionExperienceYears() {
        return professionExperienceYears;
    }

    public void setProfessionExperienceYears(Integer professionExperienceYears) {
        this.professionExperienceYears = professionExperienceYears;
    }

    public Integer getProfessionExperienceMonths() {
        return professionExperienceMonths;
    }

    public void setProfessionExperienceMonths(Integer professionExperienceMonths) {
        this.professionExperienceMonths = professionExperienceMonths;
    }

    public Integer getProfessionExperienceDays() {
        return professionExperienceDays;
    }

    public void setProfessionExperienceDays(Integer professionExperienceDays) {
        this.professionExperienceDays = professionExperienceDays;
    }

    public String getRecognizedQualificationDegree() {
        return recognizedQualificationDegree;
    }

    public void setRecognizedQualificationDegree(String recognizedQualificationDegree) {
        this.recognizedQualificationDegree = recognizedQualificationDegree;
    }

    public String getRecognizedEducationLevel() {
        return recognizedEducationLevel;
    }

    public void setRecognizedEducationLevel(String recognizedEducationLevel) {
        this.recognizedEducationLevel = recognizedEducationLevel;
    }

    public String getRecognizedProfession() {
        return recognizedProfession;
    }

    public void setRecognizedProfession(String recognizedProfession) {
        this.recognizedProfession = recognizedProfession;
    }

    public String getRecognizedArticleItem() {
        return recognizedArticleItem;
    }

    public void setRecognizedArticleItem(String recognizedArticleItem) {
        this.recognizedArticleItem = recognizedArticleItem;
    }

    public String getRecognizedArticleDirective() {
        return recognizedArticleDirective;
    }

    public void setRecognizedArticleDirective(String recognizedArticleDirective) {
        this.recognizedArticleDirective = recognizedArticleDirective;
    }

    public String getImiCorrespondence() {
        return imiCorrespondence;
    }

    public void setImiCorrespondence(String imiCorrespondence) {
        this.imiCorrespondence = imiCorrespondence;
    }

    public Integer getEducationTypeId() {
        return educationTypeId;
    }

    public void setEducationTypeId(Integer educationTypeId) {
        this.educationTypeId = educationTypeId;
    }

    public Integer getHasExperience() {
        return hasExperience;
    }

    public void setHasExperience(Integer hasExperience) {
        this.hasExperience = hasExperience;
    }

    public Integer getHasEducation() {
        return hasEducation;
    }

    public void setHasEducation(Integer hasEducation) {
        this.hasEducation = hasEducation;
    }

    public List<String> getProfessionExperienceDocuments() {
        return professionExperienceDocuments;
    }

    public void setProfessionExperienceDocuments(List<String> professionExperienceDocuments) {
        this.professionExperienceDocuments = professionExperienceDocuments;
    }

    public String getDocflowStatus() {
        return docflowStatus;
    }

    public void setDocflowStatus(String docflowStatus) {
        this.docflowStatus = docflowStatus;
    }
}
