package com.nacid.regprof.web.model.applications.report.base;

import com.nacid.data.DataConverter;
import com.nacid.data.regprof.RegprofProfessionExperienceDatesRecord;

//RayaWritten-----------------------------------------------------
public class RegprofProfExpeienceDatesForReportBaseWebModel {
    protected String dateFrom;
    protected String dateTo;
    protected String id;
    protected String workdayDuration;
    
    public RegprofProfExpeienceDatesForReportBaseWebModel(RegprofProfessionExperienceDatesRecord dates){
        dateFrom = DataConverter.formatDate(dates.getDateFrom());
        dateTo = DataConverter.formatDate(dates.getDateTo());
        id = dates.getId() + "";
        workdayDuration = dates.getWorkdayDuration() + ""; 
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getId() {
        return id;
    }

    public String getWorkdayDuration() {
        return workdayDuration;
    }
    
}
//-----------------------------------------------------------------
