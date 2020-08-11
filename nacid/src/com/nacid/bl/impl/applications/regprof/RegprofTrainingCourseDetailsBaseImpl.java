package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.applications.regprof.RegprofTrainingCourseDetailsBase;

public class RegprofTrainingCourseDetailsBaseImpl implements RegprofTrainingCourseDetailsBase {
    
    protected Integer id;
    protected String documentFname;
    protected String documentLname;
    protected String documentSname;
    protected String documentCivilId;
    protected Integer documentCivilIdType;
    protected Integer profInstitutionId;
    protected String documentNumber;
    protected String documentDate;
    protected Integer secProfQualificationId;
    protected Integer highProfQualificationId;
    protected Integer highEduLevelId;
    protected Integer sdkProfInstitutionId;
    protected Integer sdkProfQualificationId;
    protected String sdkDocumentNumber;
    protected String sdkDocumentDate;
    protected Integer educationTypeId;
    protected int hasExperience;
    protected int hasEducation;
    protected Integer documentType;
    protected Integer sdkDocumentType;
    protected Integer profInstitutionOrgNameId;
    protected Integer sdkProfInstitutionOrgNameId;
    protected Integer secCaliberId;
    protected String documentSeries;
    protected String documentRegNumber;
    protected String sdkDocumentSeries;
    protected String sdkDocumentRegNumber;
    protected Integer certificateProfQualificationId;
    protected int notRestricted;
    protected int regulatedEducationTraining;
    
    public RegprofTrainingCourseDetailsBaseImpl() { }
    
    public RegprofTrainingCourseDetailsBaseImpl(Integer id, String documentFname,
            String documentLname, String documentSname, String documentCivilId,
            Integer documentCivilIdType, Integer profInstitutionId,
            String documentNumber, String documentDate,
            Integer secProfQualificationId,
            Integer highProfQualificationId, Integer highEduLevelId,
            Integer sdkProfInstitutionId, Integer sdkProfQualificationId,
            String sdkDocumentNumber, String sdkDocumentDate,
            Integer educationTypeId, int hasExperience,
            int hasEducation, Integer documentType, Integer sdkDocumentType,
            Integer profInstitutionOrgNameId,
            Integer sdkProfInstitutionOrgNameId, Integer secCaliberId,
            String documentSeries, String documentRegNumber,
            String sdkDocumentSeries, String sdkDocumentRegNumber,
            Integer certificateProfQualificationId, int notRestricted,
            int regulatedEducationTraining) {
        this.id = id;
        this.documentFname = documentFname;
        this.documentLname = documentLname;
        this.documentSname = documentSname;
        this.documentCivilId = documentCivilId;
        this.documentCivilIdType = documentCivilIdType;
        this.profInstitutionId = profInstitutionId;
        this.documentNumber = documentNumber;
        this.documentDate = documentDate;
        this.secProfQualificationId = secProfQualificationId;
        this.highProfQualificationId = highProfQualificationId;
        this.highEduLevelId = highEduLevelId;
        this.sdkProfInstitutionId = sdkProfInstitutionId;
        this.sdkProfQualificationId = sdkProfQualificationId;
        this.sdkDocumentNumber = sdkDocumentNumber;
        this.sdkDocumentDate = sdkDocumentDate;
        this.educationTypeId = educationTypeId;
        this.hasExperience = hasExperience;
        this.hasEducation = hasEducation;
        this.documentType = documentType;
        this.sdkDocumentType = sdkDocumentType;
        this.profInstitutionOrgNameId = profInstitutionOrgNameId;
        this.sdkProfInstitutionOrgNameId = sdkProfInstitutionOrgNameId;
        this.secCaliberId = secCaliberId;
        this.documentSeries = documentSeries;
        this.documentRegNumber = documentRegNumber;
        this.sdkDocumentSeries = sdkDocumentSeries;
        this.sdkDocumentRegNumber = sdkDocumentRegNumber;
        this.certificateProfQualificationId = certificateProfQualificationId;
        this.notRestricted = notRestricted;
        this.regulatedEducationTraining = regulatedEducationTraining;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getProfInstitutionId() {
        return profInstitutionId;
    }
    public void setProfInstitutionId(Integer profInstitutionId) {
        this.profInstitutionId = profInstitutionId;
    }
    public Integer getSecProfQualificationId() {
        return secProfQualificationId;
    }
    public void setSecProfQualificationId(Integer secProfQualificationId) {
        this.secProfQualificationId = secProfQualificationId;
    }
    public Integer getHighProfQualificationId() {
        return highProfQualificationId;
    }
    public void setHighProfQualificationId(Integer highProfQualificationId) {
        this.highProfQualificationId = highProfQualificationId;
    }
    public Integer getHighEduLevelId() {
        return highEduLevelId;
    }
    public void setHighEduLevelId(Integer highEduLevelId) {
        this.highEduLevelId = highEduLevelId;
    }
    public Integer getSdkProfInstitutionId() {
        return sdkProfInstitutionId;
    }
    public void setSdkProfInstitutionId(Integer sdkProfInstitutionId) {
        this.sdkProfInstitutionId = sdkProfInstitutionId;
    }
    public Integer getSdkProfQualificationId() {
        return sdkProfQualificationId;
    }
    public void setSdkProfQualificationId(Integer sdkProfQualificationId) {
        this.sdkProfQualificationId = sdkProfQualificationId;
    }
    public Integer getEducationTypeId() {
        return educationTypeId;
    }
    public void setEducationTypeId(Integer educationTypeId) {
        this.educationTypeId = educationTypeId;
    }
    public int getHasExperience() {
        return hasExperience;
    }
    public void setHasExperience(int hasExperience) {
        this.hasExperience = hasExperience;
    }
    public int getHasEducation() {
        return hasEducation;
    }
    public void setHasEducation(int hasEducation) {
        this.hasEducation = hasEducation;
    }
    public Integer getDocumentType() {
        return documentType;
    }
    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }
    public Integer getSdkDocumentType() {
        return sdkDocumentType;
    }
    public void setSdkDocumentType(Integer sdkDocumentType) {
        this.sdkDocumentType = sdkDocumentType;
    } 
    public Integer getProfInstitutionOrgNameId() {
        return profInstitutionOrgNameId;
    }
    public void setProfInstitutionOrgNameId(Integer profInstitutionOrgNameId) {
        this.profInstitutionOrgNameId = profInstitutionOrgNameId;
    }
    public Integer getSdkProfInstitutionOrgNameId() {
        return sdkProfInstitutionOrgNameId;
    }
    public void setSdkProfInstitutionOrgNameId(Integer sdkProfInstitutionOrgNameId) {
        this.sdkProfInstitutionOrgNameId = sdkProfInstitutionOrgNameId;
    }
    public Integer getSecCaliberId() {
        return secCaliberId;
    }
    public void setSecCaliberId(Integer secCaliberId) {
        this.secCaliberId = secCaliberId;
    }
    public String getDocumentFname() {
        return documentFname;
    }
    public void setDocumentFname(String documentFname) {
        this.documentFname = documentFname;
    }
    public String getDocumentLname() {
        return documentLname;
    }
    public void setDocumentLname(String documentLname) {
        this.documentLname = documentLname;
    }
    public String getDocumentSname() {
        return documentSname;
    }
    public void setDocumentSname(String documentSname) {
        this.documentSname = documentSname;
    }
    public String getDocumentCivilId() {
        return documentCivilId;
    }
    public void setDocumentCivilId(String documentCivilId) {
        this.documentCivilId = documentCivilId;
    }
    public Integer getDocumentCivilIdType() {
        return documentCivilIdType;
    }
    public void setDocumentCivilIdType(Integer documentCivilIdType) {
        this.documentCivilIdType = documentCivilIdType;
    }
    public String getDocumentNumber() {
        return documentNumber;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    public String getDocumentDate() {
        return documentDate;
    }
    public void setDocumentDate(String documentDate) {
        this.documentDate = documentDate;
    }
    public String getSdkDocumentNumber() {
        return sdkDocumentNumber;
    }
    public void setSdkDocumentNumber(String sdkDocumentNumber) {
        this.sdkDocumentNumber = sdkDocumentNumber;
    }
    public String getSdkDocumentDate() {
        return sdkDocumentDate;
    }
    public void setSdkDocumentDate(String sdkDocumentDate) {
        this.sdkDocumentDate = sdkDocumentDate;
    }
    public String getDocumentSeries() {
        return documentSeries;
    }
    public void setDocumentSeries(String documentSeries) {
        this.documentSeries = documentSeries;
    }
    public String getDocumentRegNumber() {
        return documentRegNumber;
    }
    public void setDocumentRegNumber(String documentRegNumber) {
        this.documentRegNumber = documentRegNumber;
    }
    public String getSdkDocumentSeries() {
        return sdkDocumentSeries;
    }
    public void setSdkDocumentSeries(String sdkDocumentSeries) {
        this.sdkDocumentSeries = sdkDocumentSeries;
    }
    public String getSdkDocumentRegNumber() {
        return sdkDocumentRegNumber;
    }
    public void setSdkDocumentRegNumber(String sdkDocumentRegNumber) {
        this.sdkDocumentRegNumber = sdkDocumentRegNumber;
    }
    public Integer getCertificateProfQualificationId() {
        return certificateProfQualificationId;
    }
    public void setCertificateProfQualificationId(Integer certificateProfQualificationId) {
        this.certificateProfQualificationId = certificateProfQualificationId;
    }
    public int getNotRestricted() {
        return notRestricted;
    }
    public void setNotRestricted(int notRestricted) {
        this.notRestricted = notRestricted;
    }

    public int getRegulatedEducationTraining() {
        return regulatedEducationTraining;
    }

    public void setRegulatedEducationTraining(int regulatedEducationTraining) {
        this.regulatedEducationTraining = regulatedEducationTraining;
    }
}