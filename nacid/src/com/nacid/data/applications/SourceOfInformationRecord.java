package com.nacid.data.applications;

public class SourceOfInformationRecord {

    private int id;
    private int competentInstitutionId;
    private int universityValidityId;
    
    public SourceOfInformationRecord() {
        
    }
    
    public SourceOfInformationRecord(int id, int competentInstitutionId, int universityValidityId) {
        this.id = id;
        this.competentInstitutionId = competentInstitutionId;
        this.universityValidityId = universityValidityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompetentInstitutionId() {
        return competentInstitutionId;
    }

    public void setCompetentInstitutionId(int competentInstitutionId) {
        this.competentInstitutionId = competentInstitutionId;
    }

    public int getUniversityValidityId() {
        return universityValidityId;
    }

    public void setUniversityValidityId(int universityValidityId) {
        this.universityValidityId = universityValidityId;
    }
    
    
}
