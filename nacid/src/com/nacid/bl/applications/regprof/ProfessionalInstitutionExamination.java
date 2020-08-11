package com.nacid.bl.applications.regprof;

import java.util.Date;

//RayaWritten------------------------------------------------------
public interface ProfessionalInstitutionExamination {
    
    public Integer getId();
    public void setId(Integer id);
    public Integer getProfessionalInstitutionValidityId();
    public void setProfessionalInstitutionValidityId(
            Integer professionalInstitutionValidityId);
    public Integer getRegprofApplicationId();
    public void setRegprofApplicationId(Integer regprofApplicationId);
    public int getIsLegitimate();
    public void setIsLegitimate(int isLegitimate);
    public Date getExaminationDate();
    public void setExaminationDate(Date examinationDate);
    public String getNotes();
    public void setNotes(String notes);
    public Integer getUserCreated();
    public void setUserCreated(Integer userCreated);
}
//----------------------------------------------------------------