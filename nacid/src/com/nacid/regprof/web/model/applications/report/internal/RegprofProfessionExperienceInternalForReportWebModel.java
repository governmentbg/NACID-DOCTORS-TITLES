package com.nacid.regprof.web.model.applications.report.internal;

import com.nacid.bl.NacidDataProvider;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.regprof.web.model.applications.report.base.RegprofProfessionExperienceForReportBaseWebModel;

//RayaWritten---------------------------------------------------------
public class RegprofProfessionExperienceInternalForReportWebModel extends RegprofProfessionExperienceForReportBaseWebModel{
    public RegprofProfessionExperienceInternalForReportWebModel(RegprofProfessionExperienceRecord record, NacidDataProvider nacidDataProvider){
        super(record, nacidDataProvider);
    }
}
//-------------------------------------------------------------------------