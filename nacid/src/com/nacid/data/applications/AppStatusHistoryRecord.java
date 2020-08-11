package com.nacid.data.applications;

import java.sql.Date;

public class AppStatusHistoryRecord {

    private int id;
    private int applicationId;
    private int statusId;
    private Date dateAssigned;
    private Integer statLegalReasonId;
    private Integer sessionId;
    private int userAssigned;
    
    
    public AppStatusHistoryRecord(){
        
    }
    
    public AppStatusHistoryRecord(int id, int applicationId, int statusId, Date dateAssigned,
    		Integer statLegalReasonId, Integer sessionId, int userAssigned) {
        this.id = id;
        this.applicationId = applicationId;
        this.statusId = statusId;
        this.dateAssigned = dateAssigned;
        this.statLegalReasonId = statLegalReasonId;
        this.sessionId = sessionId;
        this.userAssigned = userAssigned;
    }

    public Integer getStatLegalReasonId() {
		return statLegalReasonId;
	}

	public void setStatLegalReasonId(Integer statLegalReasonId) {
		this.statLegalReasonId = statLegalReasonId;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Date getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

    public int getUserAssigned() {
        return userAssigned;
    }

    public void setUserAssigned(int userAssigned) {
        this.userAssigned = userAssigned;
    }
    
    
}
