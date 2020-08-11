package com.nacid.data.events;

import java.sql.Date;

public class EventRecord {

    private int id;
    private Integer eventTypeId;
    private Integer applicationId;
    private Integer docId;
    private Integer eventStatus;
    private Date reminderDate;
    private Date dueDate;
    private Integer docCategoryId;
    private Integer docTypeId;
    
    public EventRecord() {
    
    }

    public EventRecord(int id, Integer eventTypeId, Integer applicationId, 
            Integer docId, Integer eventStatus, Date reminderDate, Date dueDate,
            Integer docCategoryId, Integer docTypeId) {
        super();
        this.id = id;
        this.eventTypeId = eventTypeId;
        this.applicationId = applicationId;
        this.docId = docId;
        this.eventStatus = eventStatus;
        this.reminderDate = reminderDate;
        this.dueDate = dueDate;
        this.docCategoryId = docCategoryId;
        this.docTypeId = docTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Integer eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getDocCategoryId() {
        return docCategoryId;
    }

    public void setDocCategoryId(Integer docCategoryId) {
        this.docCategoryId = docCategoryId;
    }

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}
    
    
}
