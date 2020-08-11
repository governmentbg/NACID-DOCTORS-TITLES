package com.nacid.db.external;

import com.nacid.data.external.applications.ExtPersonDocumentRecord;
import com.nacid.data.external.applications.ExtPersonRecord;
import com.nacid.db.utils.DatabaseService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class ExtPersonDB extends DatabaseService {
    public ExtPersonDB(DataSource ds) {
        super(ds);
    }

    public ExtPersonDocumentRecord getActiveExtPersonDocumentRecord(int extPersonId) throws SQLException {
        List<ExtPersonDocumentRecord> result = selectRecords(ExtPersonDocumentRecord.class, "person_id = ? and active = ?", extPersonId, 1);
        return result.size() == 0 ? null : result.get(0);
    }

    public ExtPersonRecord getExtPerson(int id) throws SQLException {
        return selectRecord(new ExtPersonRecord(), id);
    }

    public ExtPersonRecord getExtPerson(Integer civilIdType, String civilId) throws SQLException {
        return selectRecord(ExtPersonRecord.class, "civil_id = ? and civil_id_type = ?", civilId, civilIdType);
    }

    public ExtPersonDocumentRecord addNewExtPersonDocumentRecord(ExtPersonDocumentRecord record) throws SQLException {
        execute("UPDATE eservices.person_document set active = 0 WHERE person_id = ?", record.getPersonId());
        record.setActive(1);
        return insertRecord(record);
    }

    public int persistPerson(ExtPersonRecord rec) throws SQLException {
        if (rec.getId() == 0) {
            rec = insertRecord(rec);
        } else {
//            ExtPersonRecord oldRec = getExtPerson(rec.getId());
//            if (oldRec.getUserId() != null && !Objects.equals(oldRec.getEmail(), rec.getEmail())) {
//                throw new RuntimeException("Cannot change email address of user with linked userId");
//            }
            updateRecord(rec);
        }
        return rec.getId();
    }
}
