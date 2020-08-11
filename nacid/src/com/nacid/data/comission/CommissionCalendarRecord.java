package com.nacid.data.comission;

import java.sql.Timestamp;

public class CommissionCalendarRecord {

    private int id;
    private Timestamp timestamp;
    private String notes;
    private int sessionStatusId;
    private int sessionNumber;
    public CommissionCalendarRecord() {
    }
    public CommissionCalendarRecord(int id, Timestamp timestamp, String notes, int sessionStatusId, int sessionNumber) {
        super();
        this.id = id;
        this.timestamp = timestamp;
        this.notes = notes;
        this.sessionStatusId = sessionStatusId;
        this.sessionNumber = sessionNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSessionStatusId() {
        return sessionStatusId;
    }

    public void setSessionStatusId(int sessionStatusId) {
        this.sessionStatusId = sessionStatusId;
    }
	public int getSessionNumber() {
		return sessionNumber;
	}
	public void setSessionNumber(int sessionNumber) {
		this.sessionNumber = sessionNumber;
	}
    
    
}
