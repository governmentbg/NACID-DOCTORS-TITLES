package com.nacid.regprof.web.model.applications.report.internal;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.impl.applications.regprof.RegprofTrainingCourseDetailsImpl;
import com.nacid.regprof.web.model.applications.report.base.RegprofTrainingCourseForReportBaseWebModel;

//RayaWritten-----------------------------------------------------------------
public class RegprofTrainingCourseInternalForReportWebModel extends RegprofTrainingCourseForReportBaseWebModel {
       public RegprofTrainingCourseInternalForReportWebModel(RegprofTrainingCourseDetailsImpl trainingCourseDetails, NacidDataProvider nacidDataProvider){
           super(trainingCourseDetails, nacidDataProvider);
           super.notRestricted = trainingCourseDetails.getNotRestricted() == 1;
       }
}
//----------------------------------------------------------------
