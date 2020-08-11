package com.nacid.data.applications;

/**
 * Created by georgi.georgiev on 24.08.2015.
 */
public class ApplicationExpertSpecialityRecord {
    private int id;
    private int applicationId;
    private int expertId;
    private int specialityId;

    public ApplicationExpertSpecialityRecord() {
    }

    public ApplicationExpertSpecialityRecord(int id, int applicationId, int expertId, int specialityId) {
        this.id = id;
        this.applicationId = applicationId;
        this.expertId = expertId;
        this.specialityId = specialityId;
    }

    public int getId() {
        return id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public int getExpertId() {
        return expertId;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }
}
