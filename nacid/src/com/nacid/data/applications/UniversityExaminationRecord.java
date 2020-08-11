package com.nacid.data.applications;

import java.sql.Date;

public class UniversityExaminationRecord {
    private int id;
    private int trainingCourseId;
    private int universityValidityId;
    private int userId;
    private Date examinationDate;
    private int isRecognized;
    private String notes;
    public UniversityExaminationRecord(){
    }
    public UniversityExaminationRecord(int id, int trainingCourseId, int universityValidityId, int userId, Date examinationDate, int isRecognized,
            String notes) {
        this.id = id;
        this.trainingCourseId = trainingCourseId;
        this.universityValidityId = universityValidityId;
        this.userId = userId;
        this.examinationDate = examinationDate;
        this.isRecognized = isRecognized;
        this.notes = notes;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getTrainingCourseId() {
        return trainingCourseId;
    }
    public void setTrainingCourseId(int trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }
    public int getUniversityValidityId() {
        return universityValidityId;
    }
    public void setUniversityValidityId(int universityValidityId) {
        this.universityValidityId = universityValidityId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public Date getExaminationDate() {
        return examinationDate;
    }
    public void setExaminationDate(Date examinationDate) {
        this.examinationDate = examinationDate;
    }
    public int getIsRecognized() {
        return isRecognized;
    }
    public void setIsRecognized(int isRecognized) {
        this.isRecognized = isRecognized;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public boolean isRecognized() {
        return getIsRecognized() == 1 ? true : false;
    }
    
    
}
