package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Attachment;
import com.nacid.bl.applications.DiplomaTypeAttachmentDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.DiplomaTypeAttachmentRecord;
import com.nacid.db.applications.DiplomaTypeAttachementDB;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiplomaTypeAttachmentDataProviderImpl implements DiplomaTypeAttachmentDataProvider {

	private DiplomaTypeAttachementDB db;
	private NacidDataProvider nacidDataProvider;
	public DiplomaTypeAttachmentDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
		this.db = new DiplomaTypeAttachementDB(nacidDataProvider.getDataSource());
		this.nacidDataProvider = nacidDataProvider;
	}
	
	/*@Override
	public void deleteRecordsForDiplomaType(int diplomaTypeId) {
		try {
			db.deleteRecordsForDiplomaType(diplomaTypeId);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}*/
	
	@Override
	public void deleteRecord(int id) {
		try {
			db.deleteRecord(id);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public Attachment getDiplomaTypeAttacment(int id, boolean loadContent) {
		try {
			DiplomaTypeAttachmentRecord rec = db.loadRecord(id, loadContent);
			if(rec == null) {
				return null;
			}
			return new DiplomaTypeAttachmentImpl(rec, nacidDataProvider);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public List<Attachment> getAttachmentsForDiplomaType(int diplomaTypeId) {
		try {
			ArrayList<Attachment> ret = new ArrayList<Attachment>();
			
			List<DiplomaTypeAttachmentRecord> recs = db.loadRecordsForDiplomaType(diplomaTypeId);
			for(DiplomaTypeAttachmentRecord rec :recs) {
				ret.add(new DiplomaTypeAttachmentImpl(rec, nacidDataProvider));
			}
			return ret; 
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
	
	@Override
	public int saveDiplomaTypeAttacment(int id, int diplomaTypeId, String docDescr, int docTypeId, InputStream contentStream, String fileName, String contentType, int fileSize, int userCreated) {
		try {
			System.out.println("content:"+contentType);
			DiplomaTypeAttachmentRecord rec = new DiplomaTypeAttachmentRecord(
					id, diplomaTypeId, docDescr, docTypeId, contentStream, fileName, contentType);
			rec = db.saveRecord(rec, fileSize, userCreated);
			return rec.getId();
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}
}
