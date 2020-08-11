package com.nacid.bl.applications.regprof;

import java.util.Date;

//RayaWritten----------------------------------
public interface DocumentExamination {
    public Integer getId();
    public void setId(Integer id);
    public Integer getTrainingCourseId();
    public void setTrainingCourseId(Integer trainingCourseId);
    public Date getDocumentExaminationDate();
    public void setDocumentExaminationDate(Date documentExaminationDate);
    public Integer getSource();
    public void setSource(Integer source);
    public Integer getUserCreated();
    public void setUserCreated(Integer userCreated);
    
}
//----------------------------------------------
