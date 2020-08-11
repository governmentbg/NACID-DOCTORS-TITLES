package com.ext.nacid.regprof.web.model.applications.report;

import com.nacid.bl.NacidDataProvider;
import com.nacid.data.regprof.external.ExtRegprofTrainingCourseSpecialitiesRecord;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseSpecialityForReportBaseWebModel;

//RayaWritten-----------------------------------------------------------------------------------------------------------------
public class ExtRegprofTrainingCourseSpecialitiesForReportWebModel extends RegprofTrainingCourseSpecialityForReportBaseWebModel{
    private String higherSpecialityTxt;
    private String sdkSpecialityTxt;
    private String secondarySpecialityTxt;
    public ExtRegprofTrainingCourseSpecialitiesForReportWebModel(ExtRegprofTrainingCourseSpecialitiesRecord record, NacidDataProvider nacidDataProvider){
        super(record, nacidDataProvider);
        this.higherSpecialityTxt = record.getHigherSpecialityTxt();
        this.sdkSpecialityTxt = record.getSdkSpecialityTxt();
        this.secondarySpecialityTxt = record.getSecondarySpecialityTxt();
    }
    public String getHigherSpecialityTxt() {
        return higherSpecialityTxt;
    }
    public String getSdkSpecialityTxt() {
        return sdkSpecialityTxt;
    }
    public String getSecondarySpecialityTxt() {
        return secondarySpecialityTxt;
    }    
    
}
//----------------------------------------------------------------------------------------------------------
