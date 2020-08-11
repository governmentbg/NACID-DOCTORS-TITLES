package com.nacid.bl.comision;

import java.util.Date;

public interface CommissionCalendar {

    public int getId();
    public String getNotes();
    public Date getDateAndTime();
    public int getSessionStatusId();
    public int getSessionNumber();
    
}
