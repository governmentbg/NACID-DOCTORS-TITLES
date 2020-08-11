package com.nacid.bl.regprof;

import java.util.Date;
//RayaWritten------------------------------------------
public interface RegulatedProfessionValidity {
    public Integer getId();
    public void setId(Integer id);
    public Integer getCountryId();
    public void setCountryId(Integer countryId);

    //public Integer getQualificationHighSdkId();
    //public void setQualificationHighSdkId(Integer qualificationHighSdkId);
    //public Integer getQualificationSecId();
   // public void setQualificationSecId(Integer qualificationSecId);
    //public Integer getEducationTypeId();
    //public void setEducationTypeId(Integer educationTypeId);
    public Date getExaminationDate();
    public void setExaminationDate(Date examinationDate);
    public String getNotes();
    public void setNotes(String notes);
    public String getProfession();
    public void setProfession(String profession);
    public Integer getUserCreated();
    public void setUserCreated(Integer userCreated);
    public Integer getIsRegulated();
    public void setIsRegulated(Integer isRegulated);
   //public Integer getProfessionExperienceId();
    //public void setProfessionExperienceId(Integer professionExperienceId);
    
}
//-------------------------------------------------------------
