package com.nacid.web.model.events;

import com.nacid.bl.events.EventType;

public class EventTypeWebModel {

    private int id;
    private String eventName;
    private Integer reminderDays;
    private Integer dueDays;
    private String reminderText;
    

    public EventTypeWebModel(EventType et) {
        if(et == null) {
            return;
        }
        this.id = et.getId();
        this.eventName = et.getEventName();
        this.reminderDays = et.getReminderDays();
        this.dueDays = et.getDueDays();
        this.reminderText = et.getReminderText();
    }
    
    public EventTypeWebModel(int id, String eventName, Integer reminderDays, Integer dueDays, String reminderText) {
        this.id = id;
        this.eventName = eventName;
        this.reminderDays = reminderDays;
        this.dueDays = dueDays;
        this.reminderText = reminderText;
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
