package com.nacid.db.events;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.nacid.bl.events.Event;
import com.nacid.data.events.EventRecord;
import com.nacid.db.utils.DatabaseService;

public class EventDB extends DatabaseService{

    public EventDB(DataSource ds) {
        super(ds);
    }

    public List<EventRecord> getEventsForApplication(int applId) throws SQLException {
        return selectRecords(EventRecord.class, "application_id=?", applId);
    }
    
    public List<EventRecord> getEventsForDocument(int docId, int docTypeId) throws SQLException {
        return selectRecords(EventRecord.class, "doc_id=? and doc_type_id=?", docId, docTypeId);
    }
    
    public void deleteEventsForDocument(int docId, int docTypeId) throws SQLException {
        deleteRecords(EventRecord.class, "doc_id=? and doc_type_id=?", docId, docTypeId);
    }

    public List<EventRecord> getActiveEvents() throws SQLException {
        return selectRecords(EventRecord.class, "event_status=?  order by due_date desc", Event.EVENT_BRING_DOCUMENTS);
    }
}
