package com.nacid.bl.applications.regprof;

import java.util.Date;
//RayaWritten------------------------------------------------
public interface RegulatedProfessionExamination {
    public Integer getId();
    public void setId(Integer id);
    public Integer getRegulatedValidityId();
    public void setRegulatedValidityId(
            Integer professionalInstitutionValidityId);
    public Integer getRegprofApplicationId();
    public void setRegprofApplicationId(Integer regprofApplicationId);
    public int getIsRegulated();
    public void setIsRegulated(int isRegulated);
    public Date getExaminationDate();
    public void setExaminationDate(Date examinationDate);
    public String getNotes();
    public void setNotes(String notes);
    public Integer getUserCreated();
    public void setUserCreated(Integer userCreated);
}
//-----------------------------------------------------------------