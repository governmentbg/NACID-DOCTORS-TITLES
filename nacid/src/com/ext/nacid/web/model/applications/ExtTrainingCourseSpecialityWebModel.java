package com.ext.nacid.web.model.applications;

public class ExtTrainingCourseSpecialityWebModel {
    
    private Integer specialityId;
    private String specialityTxt;
    private String specialityName;
    
    public ExtTrainingCourseSpecialityWebModel(Integer specialityId, String specialityTxt, String specialityName) {
        this.specialityId = specialityId;
        this.specialityTxt = specialityTxt;
        this.specialityName = specialityName;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public String getSpecialityTxt() {
        return specialityTxt;
    }

    public String getSpecialityName() {
        return specialityName;
    }
    
}
