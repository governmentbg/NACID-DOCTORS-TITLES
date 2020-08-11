package com.nacid.bl.impl.external.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.external.applications.ExtApplicationAttachmentDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.external.applications.ExtApplicationAttachmentRecord;
import com.nacid.db.external.applications.ExtApplicationAttachmentDB;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExtApplicationAttachmentDataProviderImpl implements ExtApplicationAttachmentDataProvider {

	protected ExtApplicationAttachmentDB db;
	protected NacidDataProvider nacidDataProvider;
	public ExtApplicationAttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
	    this(nacidDataProvider, new ExtApplicationAttachmentDB(nacidDataProvider.getDataSource()));
	}
	protected ExtApplicationAttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider, ExtApplicationAttachmentDB db) {
	    this.db = db;
	    this.nacidDataProvider = nacidDataProvider;
	}
	
	@Override
	public void deleteAttachment(int id) {
		try {
			db.deleteRecord(id);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}


	@Override
	public Attachment getApplicationAttacment(int id, boolean loadContent) {
		try {
			ExtApplicationAttachmentRecord rec = db.loadRecord(id, loadContent);
			return new ExtApplicationAttachmentImpl(rec, nacidDataProvider);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
    public List<Attachment> getAttachmentsForApplicationByType(int applicationId, int docTypeId) {
	    try {
            List<ExtApplicationAttachmentRecord> recs = db.loadRecordsForApplication(applicationId, docTypeId);
            
            List<Attachment> ret = new ArrayList<Attachment>();
            for(ExtApplicationAttachmentRecord r : recs) {
                ret.add(new ExtApplicationAttachmentImpl(r, nacidDataProvider));
            }
            return ret;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
	
	@Override
	public List<Attachment> getAttachmentsForApplication(int applicationId) {
		try {
			List<ExtApplicationAttachmentRecord> recs = db.loadRecordsForApplication(applicationId, 0);
			
			List<Attachment> ret = new ArrayList<Attachment>();
			for(ExtApplicationAttachmentRecord r : recs) {
				ret.add(new ExtApplicationAttachmentImpl(r, nacidDataProvider));
			}
			return ret;
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

	@Override
	public int saveApplicationAttacment(int id, int applicationId, String docDescr, int docTypeId, String contentType, String fileName,
			InputStream contentStream, int copyTypeId, int fileSize, int userCreated) {
		
		try {
		    ExtApplicationAttachmentRecord rec = new ExtApplicationAttachmentRecord(
					id, applicationId, docDescr, docTypeId, contentType, fileName,
					contentStream, copyTypeId);
			rec = db.saveRecord(rec, fileSize, userCreated);
			return rec.getId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	public List<ExtApplicationAttachmentRecord> getExtAttachmentRecordsForXml(int applicationId) throws SQLException {
		return db.loadRecordsForApplication(applicationId, 0);
	}

	@Override
	public void copyRecordsToInternalDB(int extApplId, int intApplId) {
	    try {
	        db.copyRecordsToInternalDB(extApplId, intApplId);
	    }
	    catch(SQLException e) {
	        throw Utils.logException(e);
	    }
	}
}
