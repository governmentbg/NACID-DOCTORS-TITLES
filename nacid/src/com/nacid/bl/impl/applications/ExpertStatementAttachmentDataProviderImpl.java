package com.nacid.bl.impl.applications;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nacid.bl.applications.ExpertStatementAttachment;
import com.nacid.bl.applications.ExpertStatementAttachmentDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.ExpertStatementAttachmentRecord;
import com.nacid.db.applications.ExpertStatementAttachementDB;

public class ExpertStatementAttachmentDataProviderImpl implements ExpertStatementAttachmentDataProvider {

	private ExpertStatementAttachementDB db;
	public ExpertStatementAttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		this.db = new ExpertStatementAttachementDB(nacidDataProvider.getDataSource());
	}
	
	

    @Override
    public ExpertStatementAttachment getExpertStatementAttachment(int id, boolean loadContent) {
        try {
            ExpertStatementAttachmentRecord rec = db.loadExpertStatement(id, loadContent);
            if(rec == null) {
                return null;
            }
            return new ExpertStatementAttachmentImpl(rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }



    @Override
    public List<ExpertStatementAttachment> getExpertStatementsForApplication(int applicationId) {
        try {
            ArrayList<ExpertStatementAttachment> ret = new ArrayList<ExpertStatementAttachment>();
            
            List<ExpertStatementAttachmentRecord> recs = db.loadExpertStatementByApplication(applicationId);
            for(ExpertStatementAttachmentRecord rec :recs) {
                ret.add(new ExpertStatementAttachmentImpl(rec));
            }
            return ret; 
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }



    @Override
    public List<ExpertStatementAttachment> getExpertStatementsForApplicationAndExpert(int applicationId, int expertId) {
        try {
            ArrayList<ExpertStatementAttachment> ret = new ArrayList<ExpertStatementAttachment>();
            
            List<ExpertStatementAttachmentRecord> recs = db.loadExpertStatementByApplicationAndExpert(applicationId, expertId);
            for(ExpertStatementAttachmentRecord rec :recs) {
                ret.add(new ExpertStatementAttachmentImpl(rec));
            }
            return ret; 
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }



    @Override
    public int saveExpertStatementAttacment(int id, int expertId, String docDescr, int docTypeId, InputStream is, String fileName,
            String contentType, int fileSize, int applicationId) {
        try {
            return db.saveExpertStatementAttachment(id, expertId, docDescr, docTypeId, is, fileName,
                    contentType, fileSize, applicationId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }



    @Override
    public void deleteRecord(int attId) {
        try {
            db.deleteExpertStatementAttachment(attId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
}
