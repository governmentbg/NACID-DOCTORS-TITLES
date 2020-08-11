package com.nacid.bl.impl.regprof.external.applications;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.regprof.external.ExtRegprofTrainingCourse;
import com.nacid.data.regprof.external.ExtRegprofProfessionExperienceRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseSpecialitiesRecord;

import java.util.List;

public class ExtRegprofTrainingCourseImpl implements ExtRegprofTrainingCourse, RequestParameterInterface {
    ExtRegprofTrainingCourseRecord details;
    List<ExtRegprofTrainingCourseSpecialitiesRecord> specialities; 
    private ExtRegprofProfessionExperienceRecord experienceRecord;
    private Integer applicationCountryId;
    private int apostilleApplication;
    public ExtRegprofTrainingCourseRecord getDetails() {
        return details;
    }

    public void setDetails(ExtRegprofTrainingCourseRecord details) {
        this.details = details;
    }

    public List<ExtRegprofTrainingCourseSpecialitiesRecord> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(
            List<ExtRegprofTrainingCourseSpecialitiesRecord> specialities) {
        this.specialities = specialities;
    }

    public ExtRegprofProfessionExperienceRecord getExperienceRecord() {
        return experienceRecord;
    }

    public void setExperienceRecord(
            ExtRegprofProfessionExperienceRecord experienceRecord) {
        this.experienceRecord = experienceRecord;
    }

    public Integer getApplicationCountryId() {
        return applicationCountryId;
    }

    public void setApplicationCountryId(Integer applicationCountryId) {
        this.applicationCountryId = applicationCountryId;
    }

    public int getApostilleApplication() {
        return apostilleApplication;
    }

    public void setApostilleApplication(int apostilleApplication) {
        this.apostilleApplication = apostilleApplication;
    }
}
