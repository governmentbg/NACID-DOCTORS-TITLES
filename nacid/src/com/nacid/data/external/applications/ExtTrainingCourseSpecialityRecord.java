package com.nacid.data.external.applications;

import com.nacid.data.annotations.Table;

@Table(name="eservices.rudi_training_course_specialities")
public class ExtTrainingCourseSpecialityRecord {
    
    private int id;
    private int extTrainingCourseId;
    private Integer specialityId;
    private String specialityTxt;
    
    public ExtTrainingCourseSpecialityRecord() { }
    
    public ExtTrainingCourseSpecialityRecord(int id, int extTrainingCourseId, Integer specialityId, String specialityTxt) {
        this.id = id;
        this.extTrainingCourseId = extTrainingCourseId;
        this.specialityId = specialityId;
        this.specialityTxt = specialityTxt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExtTrainingCourseId() {
        return extTrainingCourseId;
    }

    public void setExtTrainingCourseId(int extTrainingCourseId) {
        this.extTrainingCourseId = extTrainingCourseId;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public String getSpecialityTxt() {
        return specialityTxt;
    }

    public void setSpecialityTxt(String specialityTxt) {
        this.specialityTxt = specialityTxt;
    }
    
}
