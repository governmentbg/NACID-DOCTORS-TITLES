package com.nacid.data.regprof.external;

import com.nacid.data.annotations.Table;
import com.nacid.data.regprof.RegprofTrainingCourseSpecialityRecord;

@Table(name="eservices.regprof_training_course_specialities")
public class ExtRegprofTrainingCourseSpecialitiesRecord extends RegprofTrainingCourseSpecialityRecord {
    private String higherSpecialityTxt;
    private String sdkSpecialityTxt;
    private String secondarySpecialityTxt;
    public String getHigherSpecialityTxt() {
        return higherSpecialityTxt;
    }
    public void setHigherSpecialityTxt(String higherSpecialityTxt) {
        this.higherSpecialityTxt = higherSpecialityTxt;
    }
    public String getSdkSpecialityTxt() {
        return sdkSpecialityTxt;
    }
    public void setSdkSpecialityTxt(String sdkSpecialityTxt) {
        this.sdkSpecialityTxt = sdkSpecialityTxt;
    }
    public String getSecondarySpecialityTxt() {
        return secondarySpecialityTxt;
    }
    public void setSecondarySpecialityTxt(String secondarySpecialityTxt) {
        this.secondarySpecialityTxt = secondarySpecialityTxt;
    }
    
    
}
