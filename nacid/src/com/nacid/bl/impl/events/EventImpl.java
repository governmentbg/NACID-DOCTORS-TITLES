package com.nacid.bl.impl.events;

import java.util.Date;

import com.nacid.bl.events.Event;
import com.nacid.data.events.EventRecord;

public class EventImpl implements Event {

    
    private int id;
    private Integer eventTypeId;
    private Integer applicationId;
    private Integer docId;
    private Integer eventStatus;
    private Date reminderDate;
    private Date dueDate;
    private Integer docCategoryId;
    private Integer documentTypeId;
    
    public EventImpl(EventRecord rec) {
        this.id = rec.getId();
        this.eventTypeId = rec.getEventTypeId();
        this.applicationId = rec.getApplicationId();
        this.docId = rec.getDocId();
        this.eventStatus = rec.getEventStatus();
        this.reminderDate = rec.getReminderDate();
        this.dueDate = rec.getDueDate();
        this.docCategoryId = rec.getDocCategoryId();
        this.documentTypeId = rec.getDocTypeId();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Integer getEventTypeId() {
        return eventTypeId;
    }

    @Override
    public Integer getApplicationId() {
        return applicationId;
    }

    @Override
    public Integer getDocId() {
        return docId;
    }

    @Override
    public Integer getEventStatus() {
        return eventStatus;
    }

    @Override
    public Date getReminderDate() {
        return reminderDate;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public Integer getDocCategoryId() {
        return docCategoryId;
    }

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}
    
    
}
