package com.nacid.data.applications;

public class UniversityIdRecord {

    private int id;
    private int universityId;
    private int trainingInstId;
    
    public UniversityIdRecord() {
        
    }

    public UniversityIdRecord(int id, int universityId, int trainingInstId) {
        this.id = id;
        this.universityId = universityId;
        this.trainingInstId = trainingInstId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public int getTrainingInstId() {
        return trainingInstId;
    }

    public void setTrainingInstId(int trainingInstId) {
        this.trainingInstId = trainingInstId;
    }
    
    
}
