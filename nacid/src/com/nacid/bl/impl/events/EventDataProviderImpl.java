package com.nacid.bl.impl.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.nacid.bl.events.Event;
import com.nacid.bl.events.EventDataProvider;
import com.nacid.bl.events.EventType;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.EventStatus;
import com.nacid.data.events.EventRecord;
import com.nacid.db.events.EventDB;

public class EventDataProviderImpl implements EventDataProvider {

    private EventDB db;
    private NacidDataProviderImpl nDP;
    
    public EventDataProviderImpl(NacidDataProviderImpl nacidDataProviderImpl) {
        db = new EventDB(nacidDataProviderImpl.getDataSource());
        nDP = nacidDataProviderImpl;
    }
    
    @Override
    public Event getEvent(int id) {
        try {
            EventRecord r = db.selectRecord(new EventRecord(), id);
            if(r == null)
                return null;
            return new EventImpl(r);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<Event> getEventsForApplication(int applId) {
        try {
            List<EventRecord> recs = db.getEventsForApplication(applId);
            List<Event> ret = new ArrayList<Event>();
            if(recs != null) {
                for(EventRecord r : recs) {
                    ret.add(new EventImpl(r));
                }
            }
            return ret;
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public List<Event> getEventsForDocument(int docId, int docTypeId) {
        try {
            List<EventRecord> recs = db.getEventsForDocument(docId, docTypeId);
            List<Event> ret = new ArrayList<Event>();
            if(recs != null) {
                for(EventRecord r : recs) {
                    ret.add(new EventImpl(r));
                }
            }
            return ret;
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public void deleteEventsForDocument(int docId, int docTypeId) {
        try {
            db.deleteEventsForDocument(docId, docTypeId);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public void deleteEvent(int id) {
        try {
            db.deleteRecord(EventRecord.class, id);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public int recalculateEvent(int id, Integer eventTypeId, Integer applicationId, 
            Integer docId, Integer docCategoryId, Integer docTypeId) {
        try {
            EventType et = nDP.getEventTypeDataProvider().getEventType(eventTypeId);
            
            if(et == null) {
                throw new Exception("Invalid eventType");
            }
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, et.getReminderDays());
            Date reminderDate = cal.getTime();
            
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, et.getDueDays());
            Date dueDate = cal.getTime();
            
            
            EventRecord rec = new EventRecord(id, eventTypeId, applicationId, docId, 
                    EventStatus.STATUS_WAITING, 
                    Utils.getSqlDate(reminderDate), 
                    Utils.getSqlDate(dueDate), docCategoryId, docTypeId);
            if(id == 0) {
                rec = db.insertRecord(rec);
            }
            else {
                db.updateRecord(rec);
            }
            return rec.getId();
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
    
    @Override
    public void setStatus(int id, Integer eventStatus, Integer docCategoryId, Integer docTypeId) {
        try {
            EventRecord rec = db.selectRecord(new EventRecord(), id);
            rec.setEventStatus(eventStatus);
            rec.setDocCategoryId(docCategoryId);
            rec.setDocTypeId(docTypeId);
            db.updateRecord(rec);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<Event> getActiveEvents() {
        try {
            List<EventRecord> recs = db.getActiveEvents();
            List<Event> ret = new ArrayList<Event>();
            if(recs != null) {
                for(EventRecord r : recs) {
                    ret.add(new EventImpl(r));
                }
            }
            return ret;
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
}
