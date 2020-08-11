package com.nacid.bl.applications;

import java.util.Date;

public interface AppDocflowStatusHistory {

    public int getId();
    public int getApplicationId();
    public int getApplicationDocflowStatusId();
    public String getApplicationDocflowStatusName();
    public Date getDateAssigned();

}
