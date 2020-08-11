package com.nacid.bl.applications;

import java.sql.Date;

public interface UniversityExamination {
    public int getId();
    public int getTrainingCourseId();
    public int getUniversityValidityId();
    public UniversityValidity getUniversityValidity();
    public int getUserId();
    public Date getExaminationDate();
    public boolean isRecognized();
    public String getNotes();
}
