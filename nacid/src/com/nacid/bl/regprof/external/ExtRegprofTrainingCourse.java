package com.nacid.bl.regprof.external;

import com.nacid.data.regprof.external.ExtRegprofProfessionExperienceRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseRecord;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseSpecialitiesRecord;

import java.util.List;

public interface ExtRegprofTrainingCourse {
    public ExtRegprofTrainingCourseRecord getDetails();
    public List<ExtRegprofTrainingCourseSpecialitiesRecord> getSpecialities();
    public ExtRegprofProfessionExperienceRecord getExperienceRecord();
    public void setExperienceRecord(ExtRegprofProfessionExperienceRecord experienceRecord);
    public void setSpecialities(List<ExtRegprofTrainingCourseSpecialitiesRecord> specialities);
    public Integer getApplicationCountryId();
    public void setApplicationCountryId(Integer countryId);

    public int getApostilleApplication();

    public void setApostilleApplication(int apostilleApplication);
}
