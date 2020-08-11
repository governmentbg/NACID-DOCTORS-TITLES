package com.nacid.data.applications;

public class AvailableTrainingFormsRecord {

    private int id;
    private int universityValidityId;
    private Integer trainingFormId;
    private String notes;
    
    public AvailableTrainingFormsRecord() {
        
    }
    
    public AvailableTrainingFormsRecord(int id, int universityValidityId, Integer trainingFormId,
            String notes) {
        this.id = id;
        this.universityValidityId = universityValidityId;
        this.trainingFormId = trainingFormId;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUniversityValidityId() {
        return universityValidityId;
    }

    public void setUniversityValidityId(int universityValidityId) {
        this.universityValidityId = universityValidityId;
    }

    public Integer getTrainingFormId() {
        return trainingFormId;
    }

    public void setTrainingFormId(Integer trainingFormId) {
        this.trainingFormId = trainingFormId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
}
