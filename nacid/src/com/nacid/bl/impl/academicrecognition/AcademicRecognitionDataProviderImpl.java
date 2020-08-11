package com.nacid.bl.impl.academicrecognition;

import java.sql.SQLException;
import java.util.List;

import com.nacid.bl.academicrecognition.AcademicRecognitionDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.db.academicrecognition.AcademicRecognitionDB;

public class AcademicRecognitionDataProviderImpl implements AcademicRecognitionDataProvider {

    private NacidDataProviderImpl nacidDataProvider;
    private AcademicRecognitionDB db;
    public AcademicRecognitionDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new AcademicRecognitionDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    @Override
    public BGAcademicRecognitionInfoImpl saveBGAcademicRecognitionRecord(BGAcademicRecognitionInfoImpl info) {
        try {
            if (info.getId() == null) {
                info = db.insertRecord(info);
            } else {
                db.updateRecord(info);
            }
            return info;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    @Override
    public BGAcademicRecognitionInfoImpl getAcademicRecognition(int id) {
        try {
            return db.selectRecord(new BGAcademicRecognitionInfoImpl(), id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    public List<BGAcademicRecognitionInfoImpl> getAcademicRecognitions() {
        try {
            return db.selectRecords(BGAcademicRecognitionInfoImpl.class, null);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    @Override
    public void deleteBGAcademicRecognitionRecord(int id) {
        try {
            db.deleteRecord(BGAcademicRecognitionInfoImpl.class, id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
    }
    public List<BGAcademicRecognitionExtendedImpl> getAcademicReconitionExtended() { 
        try {
            return db.getBGAcademicRecognitionExtendedRecords();
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
	@Override
	public List<BGAcademicRecognitionExtendedImpl> getSimilarAcademicRecognitions(
			String applicant, String university, String universityCountry,
			String oks, String diplomaSpeciality, Integer withoutId) {
		try {
			return db.getBgAcademicRecognitionSimilarRecords(applicant, university, universityCountry, oks, diplomaSpeciality, withoutId);
		} catch (SQLException e) {
			 throw Utils.logException(e);
		}
	}

}
