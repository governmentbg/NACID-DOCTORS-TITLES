package com.nacid.bl.applications.regprof;


public interface RegprofTrainingCourseDetailsBase {
    public Integer getId();
    public void setId(Integer id);    
    public Integer getProfInstitutionId();
    public void setProfInstitutionId(Integer profInstitutionId);    
    public Integer getSecProfQualificationId();
    public void setSecProfQualificationId(Integer secProfQualificationId);
    public Integer getHighProfQualificationId();
    public void setHighProfQualificationId(Integer highProfQualificationId);
    public Integer getHighEduLevelId();
    public void setHighEduLevelId(Integer highEduLevelId);
    public Integer getSdkProfInstitutionId();
    public void setSdkProfInstitutionId(Integer sdkProfInstitutionId);
    public Integer getSdkProfQualificationId();
    public void setSdkProfQualificationId(Integer sdkProfQualificationId);   
    public Integer getEducationTypeId();
    public void setEducationTypeId(Integer educationTypeId);
    public int getHasExperience();
    public void setHasExperience(int hasExperience);
    public int getHasEducation();
    public void setHasEducation(int hasEducation);
    public Integer getDocumentType();
    public void setDocumentType(Integer documentType);
    public Integer getSdkDocumentType();
    public void setSdkDocumentType(Integer sdkDocumentType); 
    public Integer getProfInstitutionOrgNameId();
    public void setProfInstitutionOrgNameId(Integer profInstitutionOrgNameId);
    public Integer getSdkProfInstitutionOrgNameId();
    public void setSdkProfInstitutionOrgNameId(Integer sdkProfInstitutionOrgNameId);
    public Integer getSecCaliberId();
    public void setSecCaliberId(Integer secCaliberId);
    public String getDocumentFname();
    public void setDocumentFname(String documentFname);
    public String getDocumentLname();
    public void setDocumentLname(String documentLname);
    public String getDocumentSname();
    public void setDocumentSname(String documentSname);
    public String getDocumentCivilId();
    public void setDocumentCivilId(String documentCivilId);
    public Integer getDocumentCivilIdType();
    public void setDocumentCivilIdType(Integer documentCivilIdType);
    public String getDocumentNumber();
    public void setDocumentNumber(String documentNumber);
    public String getDocumentDate();
    public void setDocumentDate(String documentDate);
    public String getSdkDocumentNumber();
    public void setSdkDocumentNumber(String sdkDocumentNumber);
    public String getSdkDocumentDate();
    public void setSdkDocumentDate(String sdkDocumentDate);
    public String getDocumentSeries();
    public void setDocumentSeries(String documentSeries);
    public String getDocumentRegNumber();
    public void setDocumentRegNumber(String documentRegNumber);
    public String getSdkDocumentSeries();
    public void setSdkDocumentSeries(String sdkDocumentSeries);
    public String getSdkDocumentRegNumber();
    public void setSdkDocumentRegNumber(String sdkDocumentRegNumber);
    public Integer getCertificateProfQualificationId();
    public void setCertificateProfQualificationId(Integer certificateProfQualificationId);
    public int getNotRestricted();
    public void setNotRestricted(int notRestricted);
}
