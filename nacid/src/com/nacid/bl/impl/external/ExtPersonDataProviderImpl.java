package com.nacid.bl.impl.external;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtPerson;
import com.nacid.bl.external.ExtPersonDataProvider;
import com.nacid.bl.external.ExtPersonDocument;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.external.applications.ExtPersonDocumentRecord;
import com.nacid.data.external.applications.ExtPersonRecord;
import com.nacid.db.external.ExtPersonDB;

public class ExtPersonDataProviderImpl implements ExtPersonDataProvider {

    private ExtPersonDB db; 
    private NacidDataProvider nacidDataProvider;
    public ExtPersonDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new ExtPersonDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    
    
    @Override
    public ExtPerson getExtPerson(int id) {
        try {
            return new ExtPersonImpl(db.getExtPerson(id), nacidDataProvider);
        }
        catch (Exception e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public ExtPerson getExtPerson(int civilIdType, String civilId) {
        try {
            ExtPersonRecord res = db.getExtPerson(civilIdType, civilId);
            return res == null ? null : new ExtPersonImpl(res, nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }

    }

    @Override
    public ExtPerson getExtPersonByUserId(int userId) {
        try {
            List<ExtPersonRecord> recs = db.selectRecords(
                    ExtPersonRecord.class, "user_id=?", userId);
            ExtPerson rec = null;
            if(recs != null && recs.size() > 0) {
                rec = new ExtPersonImpl(recs.get(0), nacidDataProvider);
            }
            return rec;
        }
        catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    public ExtPersonDocument getExtPersonActiveDocument(int extPersonId) {
        try {
            return db.getActiveExtPersonDocumentRecord(extPersonId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public ExtPersonDocument getExtPersonDocument(int documentId) {
        try {
            return db.selectRecord(new ExtPersonDocumentRecord(), documentId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public ExtPersonDocumentRecord insertNewExtPersonDocument(ExtPersonDocumentRecord record) {
            try {
                return db.addNewExtPersonDocumentRecord(record);
            } catch (SQLException e) {
                throw Utils.logException(e);
            }
    }
    @Override
    public int saveExtPerson(int id, String fname, String sname, String lname, String civilId, Integer civilIdType, Integer birthCountryId,
            String birthCity, Date birthDate, Integer citizenshipId, String email,
            String hashCode, Integer userId) {
        
        if(fname != null) fname = fname.toUpperCase();
        if(sname != null) sname = sname.toUpperCase();
        if(lname != null) lname = lname.toUpperCase();
        
        
        try {
            ExtPersonRecord rec = new ExtPersonRecord(id, fname, sname, lname, civilId, civilIdType, birthCountryId,
                    birthCity, Utils.getSqlDate(birthDate), citizenshipId, 
                    email, hashCode, userId);
            return db.persistPerson(rec);
        }
        catch (Exception e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public ExtPerson activateExtPerson(String hashCode) {
        try {
            List<ExtPersonRecord> recs = db.selectRecords(
                    ExtPersonRecord.class, "hash_code=?", hashCode);
            if(recs == null || recs.size() == 0) {
                return null;
            }
            else {
                ExtPersonRecord rec = recs.get(0);
                rec.setHashCode(null);
                db.updateRecord(rec);
                return new ExtPersonImpl(rec, nacidDataProvider);
            }
        }
        catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    
    public ExtPersonDB getDb() {
        return db;
    }
}
