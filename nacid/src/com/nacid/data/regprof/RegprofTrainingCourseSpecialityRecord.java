package com.nacid.data.regprof;

import com.nacid.data.annotations.Table;

@Table(name="regprof.training_course_specialities")
public class RegprofTrainingCourseSpecialityRecord {

    private Integer id;
    private Integer trainingCourseId;
    private Integer secondarySpecialityId;
    private Integer higherSpecialityId;
    private Integer sdkSpecialityId;

    public RegprofTrainingCourseSpecialityRecord(Integer id, Integer trainingCourseId, Integer secondarySpecialityId, Integer higherSpecialityId, Integer sdkSpecialityId) {
        this.id = id;
        this.trainingCourseId = trainingCourseId;
        this.secondarySpecialityId = secondarySpecialityId;
        this.higherSpecialityId = higherSpecialityId;
        this.sdkSpecialityId = sdkSpecialityId;
    }

    public RegprofTrainingCourseSpecialityRecord() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrainingCourseId() {
        return trainingCourseId;
    }

    public void setTrainingCourseId(Integer trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }

    public Integer getSecondarySpecialityId() {
        return secondarySpecialityId;
    }

    public void setSecondarySpecialityId(Integer secondarySpecialityId) {
        this.secondarySpecialityId = secondarySpecialityId;
    }

    public Integer getHigherSpecialityId() {
        return higherSpecialityId;
    }

    public void setHigherSpecialityId(Integer higherSpecialityId) {
        this.higherSpecialityId = higherSpecialityId;
    }

    public Integer getSdkSpecialityId() {
        return sdkSpecialityId;
    }

    public void setSdkSpecialityId(Integer sdkSpecialityId) {
        this.sdkSpecialityId = sdkSpecialityId;
    }

}
