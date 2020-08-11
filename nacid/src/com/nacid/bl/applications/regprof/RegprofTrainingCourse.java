package com.nacid.bl.applications.regprof;

import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;

public interface RegprofTrainingCourse {
    
    public RegprofProfessionExperienceRecord getExperienceRecord();
    public void setExperienceRecord(RegprofProfessionExperienceRecord experienceRecord);
    //public List<RegprofProfessionExperienceDatesRecord> getDatesRecords();
    //public void setDatesRecords(List<RegprofProfessionExperienceDatesRecord> datesRecords);
    public RegprofTrainingCourseDetailsImpl getDetails();
    public void setDetails(RegprofTrainingCourseDetailsImpl details);
    
}
