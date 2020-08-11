package com.nacid.bl.impl.applications;

import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.applications.DiplomaExamination;
import com.nacid.data.applications.DiplomaExaminationRecord;

public class DiplomaExaminationImpl implements DiplomaExamination{
    private int id;
    private int trainingCourseId;
    private int userId;
    private Date examinationDate;
    private String notes;
    private boolean isRecognized;
    private Integer competentInstitutionId;
    private boolean isInstitutionCommunicated;
    private boolean isUniversityCommunicated;
    private NacidDataProvider nacidDataProvider;
    private boolean foundInRegister;
    public DiplomaExaminationImpl(DiplomaExaminationRecord record, NacidDataProvider nacidDataProvider) {
        this.id = record.getId();
        this.trainingCourseId = record.getTrainingCourseId();
        this.userId = record.getUserId();
        this.examinationDate = record.getSqlExaminationDate();
        this.notes = record.getNotes();
        this.isRecognized = record.getIntIsRecognized() == 1 ? true : false;
        this.competentInstitutionId = record.getCompetentInstitutionId();
        this.isInstitutionCommunicated = record.getIntIsInstitutionCommunicated() == 1 ? true : false;
        this.isUniversityCommunicated = record.getIntIsUniversityCommunicated() == 1 ? true : false;
        this.nacidDataProvider = nacidDataProvider;
        this.foundInRegister = record.getIntIsFoundInRegister() == 1;
    }
    public int getId() {
        return id;
    }
    public int getTrainingCourseId() {
        return trainingCourseId;
    }
    public int getUserId() {
        return userId;
    }
    public String getNotes() {
        return notes;
    }
    public Integer getCompetentInstitutionId() {
        return competentInstitutionId;
    }
    public java.util.Date getExaminationDate() {
        return examinationDate;
    }
    public boolean isInstitutionCommunicated() {
        return isInstitutionCommunicated;
    }
    
    public boolean isRecognized() {
        return isRecognized;
    }
    
    public boolean isUniversityCommunicated() {
        return isUniversityCommunicated;
    }
	
	public CompetentInstitution getCompetentInstitution() {
		return getCompetentInstitutionId() == null ? null : nacidDataProvider.getCompetentInstitutionDataProvider().getCompetentInstitution(getCompetentInstitutionId());
	}

    @Override
    public boolean isFoundInRegister() {
        return foundInRegister;
    }
}
