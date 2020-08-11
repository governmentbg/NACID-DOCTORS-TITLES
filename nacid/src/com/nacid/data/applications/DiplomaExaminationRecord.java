package com.nacid.data.applications;

import java.sql.Date;

public class DiplomaExaminationRecord {
    private int id;
    private int trainingCourseId;
    private int userId;
    private Date sqlExaminationDate;
    private String notes;
    private int intIsRecognized;
    private Integer competentInstitutionId;
    private int intIsInstitutionCommunicated;
    private int intIsUniversityCommunicated;
    private int intIsFoundInRegister;
    public DiplomaExaminationRecord(){
    }
    public DiplomaExaminationRecord(int id, int trainingCourseId, int userId, Date sqlExaminationDate, String notes, int intIsRecognized,
            Integer competentInstitutionId, int intIsInstitutionCommunicated, int intIsUniversityCommunicated, int intIsFoundInRegister) {
        this.id = id;
        this.trainingCourseId = trainingCourseId;
        this.userId = userId;
        this.sqlExaminationDate = sqlExaminationDate;
        this.notes = notes;
        this.intIsRecognized = intIsRecognized;
        this.competentInstitutionId = competentInstitutionId;
        this.intIsInstitutionCommunicated = intIsInstitutionCommunicated;
        this.intIsUniversityCommunicated = intIsUniversityCommunicated;
        this.intIsFoundInRegister = intIsFoundInRegister;
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
    public Date getSqlExaminationDate() {
        return sqlExaminationDate;
    }
    public String getNotes() {
        return notes;
    }
    public int getIntIsRecognized() {
        return intIsRecognized;
    }
    public Integer getCompetentInstitutionId() {
        return competentInstitutionId;
    }
    public int getIntIsInstitutionCommunicated() {
        return intIsInstitutionCommunicated;
    }
    public int getIntIsUniversityCommunicated() {
        return intIsUniversityCommunicated;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTrainingCourseId(int trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setSqlExaminationDate(Date sqlExaminationDate) {
        this.sqlExaminationDate = sqlExaminationDate;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setIntIsRecognized(int intIsRecognized) {
        this.intIsRecognized = intIsRecognized;
    }
    public void setCompetentInstitutionId(Integer competentInstitutionId) {
        this.competentInstitutionId = competentInstitutionId;
    }
    public void setIntIsInstitutionCommunicated(int intIsInstitutionCommunicated) {
        this.intIsInstitutionCommunicated = intIsInstitutionCommunicated;
    }
    public void setIntIsUniversityCommunicated(int intIsUniversityCommunicated) {
        this.intIsUniversityCommunicated = intIsUniversityCommunicated;
    }

    public int getIntIsFoundInRegister() {
        return intIsFoundInRegister;
    }

    public void setIntIsFoundInRegister(int intIsFoundInRegister) {
        this.intIsFoundInRegister = intIsFoundInRegister;
    }
}
