package com.ext.nacid.regprof.web.model.applications.report;

import com.nacid.bl.NacidDataProvider;
import com.nacid.data.regprof.external.ExtRegprofProfessionExperienceRecord;
import com.nacid.regprof.web.model.applications.report.base.RegprofProfessionExperienceForReportBaseWebModel;
//RayaWritten-----------------------------------------------------------------------------------------------------------
public class ExtRegprofProfessionExprienceForReportWebModel extends RegprofProfessionExperienceForReportBaseWebModel{
    private String professionExperienceTxt;
    public ExtRegprofProfessionExprienceForReportWebModel(ExtRegprofProfessionExperienceRecord record, NacidDataProvider nacidDataProvider){
        super(record, nacidDataProvider);
        if(record != null){
            this.professionExperienceTxt = record.getProfessionExperienceTxt();
        }
    }
    public String getProfessionExperienceTxt() {
        return professionExperienceTxt;
    }   
}
//------------------------------------------------------------------------------------------------------------
