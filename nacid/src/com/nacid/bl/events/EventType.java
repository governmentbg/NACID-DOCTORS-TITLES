package com.nacid.bl.events;

public interface EventType {

    public int getId();

    public String getEventName();

    public Integer getReminderDays();

    public Integer getDueDays();

    public String getReminderText();

   
}
