package com.nacid.bl.impl.academicrecognition;

import java.sql.SQLException;

import com.nacid.bl.academicrecognition.BGAcademicRecognitionFileLog;
import com.nacid.bl.academicrecognition.BGAcademicRecognitionLogDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.db.academicrecognition.BGAcademicRecognitionLogDB;

public class BGAcademicRecognitionLogDataProviderImpl implements BGAcademicRecognitionLogDataProvider {

	private BGAcademicRecognitionLogDB db;
	private NacidDataProviderImpl nacidDataProvider;	
	
	public BGAcademicRecognitionLogDataProviderImpl(
			NacidDataProviderImpl nacidDataProvider) {
		super();
		this.nacidDataProvider = nacidDataProvider;
		this.db = new BGAcademicRecognitionLogDB(nacidDataProvider.getDataSource());
	}



	@Override
	public BGAcademicRecognitionFileLog saveBGAcademicRecognitionFileLog(
			BGAcademicRecognitionFileLog log) {
		try {
			return db.insertRecord(log);
		} catch (SQLException e) {
			throw Utils.logException(e);
		}
	}

}
