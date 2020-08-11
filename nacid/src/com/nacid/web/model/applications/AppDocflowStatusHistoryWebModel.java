package com.nacid.web.model.applications;

import com.nacid.bl.applications.AppDocflowStatusHistory;
import com.nacid.data.DataConverter;

public class AppDocflowStatusHistoryWebModel {

    private String statusName;
    private String dateAssigned;

    public AppDocflowStatusHistoryWebModel(AppDocflowStatusHistory appStatusHistory) {
        statusName = appStatusHistory.getApplicationDocflowStatusName();
        this.dateAssigned = DataConverter.formatDate(appStatusHistory.getDateAssigned());
    }

    public String getStatusName() {
        return statusName;
    }

    public String getDateAssigned() {
        return dateAssigned;
    }
}
