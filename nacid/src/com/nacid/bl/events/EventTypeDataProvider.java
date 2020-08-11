package com.nacid.bl.events;

import java.util.List;

public interface EventTypeDataProvider {

    public EventType getEventType(int id);
    public List<EventType> getEventTypes();
    public int saveEventType(int id, String eventName, Integer reminderDays, Integer dueDays, String reminderText);
    
}
