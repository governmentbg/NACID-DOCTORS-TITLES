package com.nacid.data.events;


public class EventTypeRecord {

    private int id;
    private String eventName;
    private Integer reminderDays;
    private Integer dueDays;
    private String reminderText;
    
    public EventTypeRecord() {
        
    }

    public EventTypeRecord(int id, String eventName, Integer reminderDays, Integer dueDays, String reminderText) {
        this.id = id;
        this.eventName = eventName;
        this.reminderDays = reminderDays;
        this.dueDays = dueDays;
        this.reminderText = reminderText;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(Integer reminderDays) {
        this.reminderDays = reminderDays;
    }

    public Integer getDueDays() {
        return dueDays;
    }

    public void setDueDays(Integer dueDays) {
        this.dueDays = dueDays;
    }

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }
    
    
}
