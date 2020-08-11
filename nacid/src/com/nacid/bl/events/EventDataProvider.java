package com.nacid.bl.events;

import java.util.List;

public interface EventDataProvider {

    public Event getEvent(int id);

    public List<Event> getEventsForApplication(int applId);

    public List<Event> getEventsForDocument(int docId, int docTypeId);

    public void deleteEventsForDocument(int docId, int docTypeId);

    public void deleteEvent(int id);

    public int recalculateEvent(int id, Integer eventTypeId, Integer applicationId, Integer docId, Integer docCategoryId, Integer docTypeId);

    public void setStatus(int id, Integer eventStatus, Integer docCategoryId, Integer docTypeId);

    public List<Event> getActiveEvents();

}
