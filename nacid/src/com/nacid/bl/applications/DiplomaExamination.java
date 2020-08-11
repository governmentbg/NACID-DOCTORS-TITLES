package com.nacid.bl.applications;

import java.util.Date;

public interface DiplomaExamination {
    public int getId();
    public int getUserId();
    public Date getExaminationDate();
    public String getNotes();
    public boolean isRecognized();
    public Integer getCompetentInstitutionId();
    public CompetentInstitution getCompetentInstitution();
    public boolean isInstitutionCommunicated();
    public boolean isUniversityCommunicated();
    boolean isFoundInRegister();
}
