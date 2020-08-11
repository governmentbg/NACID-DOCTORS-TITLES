package com.nacid.bl.impl.comission;

import java.util.Date;

import com.nacid.bl.comision.CommissionCalendar;
import com.nacid.data.comission.CommissionCalendarRecord;

public class CommissionCalendarImpl implements CommissionCalendar {

    private int id;
    private Date dateAndTime;
    private String notes;
    private int sessionStatusId;
    private int sessionNumber;
    
    public CommissionCalendarImpl(CommissionCalendarRecord record) {
        this.id = record.getId();
        this.dateAndTime = new Date(record.getTimestamp().getTime());
        this.notes = record.getNotes();
        this.sessionStatusId = record.getSessionStatusId();
        this.sessionNumber = record.getSessionNumber();
    }
    
    public CommissionCalendarImpl(int id, Date dateAndTime, String notes, int sessionStatus, int sessionNumber) {
        this.id = id;
        this.dateAndTime = dateAndTime;
        this.notes = notes;
        this.sessionStatusId = sessionStatus;
        this.sessionNumber = sessionNumber;
    }

    @Override
    public Date getDateAndTime() {
        return dateAndTime;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public int getSessionStatusId() {
        return sessionStatusId;
    }
    public int getSessionNumber() {
    	return sessionNumber;
    }

}
