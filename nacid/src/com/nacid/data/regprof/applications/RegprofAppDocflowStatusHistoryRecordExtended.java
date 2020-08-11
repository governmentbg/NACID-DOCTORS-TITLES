package com.nacid.data.regprof.applications;

import com.nacid.data.DataConverter;

/**
 * Created by georgi.georgiev on 15.04.2015.
 */
public class RegprofAppDocflowStatusHistoryRecordExtended extends RegprofAppDocflowStatusHistoryRecord {

    private String applicationDocflowStatusName;



    public String getApplicationDocflowStatusName() {
        return applicationDocflowStatusName;
    }

    public void setApplicationDocflowStatusName(String applicationDocflowStatusName) {
        this.applicationDocflowStatusName = applicationDocflowStatusName;
    }

    public String getDateAssignedFormatted(){
        return DataConverter.formatDate(getDateAssigned());
    }
}
