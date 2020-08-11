package com.nacid.bl.impl.applications;

import com.nacid.bl.applications.AppDocflowStatusHistory;
import com.nacid.data.applications.AppDocflowStatusHistoryRecordExtended;

import java.util.Date;

/**
 * Created by georgi.georgiev on 15.04.2015.
 */
public class AppDocflowStatusHistoryImpl implements AppDocflowStatusHistory {
    private AppDocflowStatusHistoryRecordExtended record;

    public AppDocflowStatusHistoryImpl(AppDocflowStatusHistoryRecordExtended record) {
        this.record = record;
    }

    @Override
    public int getId() {
        return record.getId();
    }

    @Override
    public int getApplicationId() {
        return record.getApplicationId();
    }

    @Override
    public int getApplicationDocflowStatusId() {
        return record.getAppStatusDocflowId();
    }

    @Override
    public String getApplicationDocflowStatusName() {
        return record.getApplicationDocflowStatusName();
    }

    @Override
    public Date getDateAssigned() {
        return record.getDateAssigned();
    }
}
