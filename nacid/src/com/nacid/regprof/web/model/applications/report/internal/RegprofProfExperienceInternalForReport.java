package com.nacid.regprof.web.model.applications.report.internal;

import com.nacid.bl.NacidDataProvider;
import com.nacid.data.regprof.RegprofProfessionExperienceRecord;
import com.nacid.regprof.web.model.applications.report.base.RegprofProfessionExperienceForReportBaseWebModel;

//RayaWritten---------------------------------------------------------
public class RegprofProfExperienceInternalForReport extends RegprofProfessionExperienceForReportBaseWebModel{
    public RegprofProfExperienceInternalForReport(RegprofProfessionExperienceRecord record, NacidDataProvider nacidDataProvider){
        super(record, nacidDataProvider);
    }
}
//--------------------------------------------------------------------