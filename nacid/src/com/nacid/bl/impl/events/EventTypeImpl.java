package com.nacid.bl.impl.events;

import com.nacid.bl.events.EventType;
import com.nacid.data.events.EventTypeRecord;

public class EventTypeImpl implements EventType {

    private int id;
    private String eventName;
    private Integer reminderDays;
    private Integer dueDays;
    private String reminderText;
    

    protected EventTypeImpl(EventTypeRecord r) {
        this.id = r.getId();
        this.eventName = r.getEventName();
        this.reminderDays = r.getReminderDays();
        this.dueDays = r.getDueDays();
        this.reminderText = r.getReminderText();
    }


    public int getId() {
        return id;
    }


    public String getEventName() {
        return eventName;
    }


    public Integer getReminderDays() {
        return reminderDays;
    }


    public Integer getDueDays() {
        return dueDays;
    }


    public String getReminderText() {
        return reminderText;
    }
    
    
}
