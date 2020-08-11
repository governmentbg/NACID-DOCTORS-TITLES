package com.nacid.bl.impl.events;

import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.events.EventType;
import com.nacid.bl.events.EventTypeDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.events.EventTypeRecord;
import com.nacid.db.events.EventTypeDB;

public class EventTypeDataProviderImpl implements EventTypeDataProvider {

    private EventTypeDB db;
    
    public EventTypeDataProviderImpl(NacidDataProviderImpl nacidDataProviderImpl) {
        db = new EventTypeDB(nacidDataProviderImpl.getDataSource());
    }
    
    @Override
    public EventType getEventType(int id) {
        try {
            EventTypeRecord rec = db.selectRecord(new EventTypeRecord(), id);
            return new EventTypeImpl(rec);
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<EventType> getEventTypes() {
        try {
            List<EventTypeRecord> recs = db.selectRecords(EventTypeRecord.class, null);
            List<EventType> ret = new ArrayList<EventType>();
            if(recs != null) {
                for(EventTypeRecord rec : recs) {
                    ret.add(new EventTypeImpl(rec));
                }
            }
            return ret;
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public int saveEventType(int id, String eventName, Integer reminderDays, Integer dueDays, String reminderText) {
        EventTypeRecord r = new EventTypeRecord(id, eventName, reminderDays, dueDays, reminderText);
        
        try {
            if(id == 0) {
                r = db.insertRecord(r);
            }
            else {
                db.updateRecord(r);
            }
            return r.getId();
        }
        catch(Exception e) {
            throw Utils.logException(e);
        }
    }
}
