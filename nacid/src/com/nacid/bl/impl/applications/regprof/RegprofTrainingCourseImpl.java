package com.nacid.bl.impl.applications.regprof;

import com.nacid.bl.RequestParameterInterface;
import com.nacid.bl.applications.regprof.RegprofTrainingCourse;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;

public class RegprofTrainingCourseImpl implements RegprofTrainingCourse, RequestParameterInterface {

    private RegprofProfessionExperienceRecord experienceRecord;
    //private List<RegprofProfessionExperienceDatesRecord> datesRecords = new AutoPopulatingList<RegprofProfessionExperienceDatesRecord>(RegprofProfessionExperienceDatesRecord.class);
    private RegprofTrainingCourseDetailsImpl details;

    public RegprofTrainingCourseImpl() {
        this.experienceRecord = null;
        //this.datesRecords = new AutoPopulatingList<RegprofProfessionExperienceDatesRecord>(RegprofProfessionExperienceDatesRecord.class);
        this.details = null;
    }

    public RegprofProfessionExperienceRecord getExperienceRecord() {
        return experienceRecord;
    }

    public void setExperienceRecord(RegprofProfessionExperienceRecord experienceRecord) {
        this.experienceRecord = experienceRecord;
    }

    /*public List<RegprofProfessionExperienceDatesRecord> getDatesRecords() {
        return datesRecords; 
    }

    public void setDatesRecords(List<RegprofProfessionExperienceDatesRecord> datesRecords) {
        this.datesRecords = datesRecords;
    }*/

    public RegprofTrainingCourseDetailsImpl getDetails() {
        return details;
    }

    public void setDetails(RegprofTrainingCourseDetailsImpl details) {
        this.details = details;
    }

}
