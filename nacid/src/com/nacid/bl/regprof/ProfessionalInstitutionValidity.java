package com.nacid.bl.regprof;

import java.util.Date;

//RayaWritten---------------------------------------
public interface ProfessionalInstitutionValidity {
    
    public Integer getId();
    public void setId(Integer id);
    public Date getExaminationDate();
    public void setExaminationDate(Date examinationDate);
    public Integer getProfessionalInstitutionId();
    public void setProfessionalInstitutionId(Integer professionalInstitutionId);
    public Integer getHasRightsEducate();
    public void setHasRightsEducate(Integer hasRightsEducate);
    public Integer getIsLegitimate();
    public void setIsLegitimate(Integer isLegitimate);
    public Integer getUserCreated();
    public void setUserCreated(Integer userCreated);
    public String getNotes();
    public void setNotes(String notes);
    public Integer getQualificationBulgariaHighSdkId();
    public void setQualificationBulgariaHighSdkId(
            Integer qualificationBulgariaHighSdkId);
    public Integer getQualificationBulgariaSecId();
    public void setQualificationBulgariaSecId(Integer qualificationBulgariaSecId);
}
//--------------------------------------------------