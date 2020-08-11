package com.nacid.data.applications;

import com.nacid.data.annotations.Table;

@Table(name="training_course_specialities")
public class TrainingCourseSpecialityRecord {
    private int id;
    private int trainingCourseId;
    private int specialityId;
    private Integer originalSpecialityId;

    public TrainingCourseSpecialityRecord() {
    }

    public TrainingCourseSpecialityRecord(int id, int trainingCourseId, int specialityId, Integer originalSpecialityId) {
        this.id = id;
        this.trainingCourseId = trainingCourseId;
        this.specialityId = specialityId;
        this.originalSpecialityId = originalSpecialityId;
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
    public int getSpecialityId() {
        return specialityId;
    }
    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    public Integer getOriginalSpecialityId() {
        return originalSpecialityId;
    }

    public void setOriginalSpecialityId(Integer originalSpecialityId) {
        this.originalSpecialityId = originalSpecialityId;
    }
}
