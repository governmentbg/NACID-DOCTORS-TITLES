package com.nacid.db.academicrecognition;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.nacid.bl.impl.academicrecognition.BGAcademicRecognitionExtendedImpl;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.StandAloneDataSource;

public class AcademicRecognitionDB extends DatabaseService {
	private static final String SELECT_BG_ACADEMIC_RECOGNITION_RECORDS_EXTENDED = "select ari.*, uny.bg_name as recognized_university_name, st.name as recognition_status_name from " + 
	                                                                                " bg_academic_recognition_info ari " +
	                                                                                " join university uny on (uny.id = ari.recognized_university_id) "
	                                                                                + " join nomenclatures.bg_academic_recognition_status st on (st.id = ari.recognition_status_id) "
	                                                                                + " ORDER BY ari.id DESC";
	private static final String SELECT_BG_ACADEMIC_RECOGNITION_SIMILAR_RECORDS_EXTENDED = "select ari.*, uny.bg_name as recognized_university_name, st.name as recognition_status_name from bg_academic_recognition_info ari " +
			" join university uny on (uny.id = ari.recognized_university_id) "+
            " join nomenclatures.bg_academic_recognition_status st on (st.id = ari.recognition_status_id) "+
            " WHERE  applicant= ? and university = ? and university_country =? and education_level = ? and diploma_speciality = ? "
            + " ORDER BY ari.id DESC ";

	private static final String SELECT_BG_ACADEMIC_RECOGNITION_SIMILAR_RECORDS_NOT_ID_EXTENDED = "select ari.*, uny.bg_name as recognized_university_name, st.name as recognition_status_name "
			+ " from bg_academic_recognition_info ari " +
			" join university uny on (uny.id = ari.recognized_university_id) "+
            " join nomenclatures.bg_academic_recognition_status st on (st.id = ari.recognition_status_id) "+
            " WHERE  ari.id <> ? and  applicant= ? and university = ? and university_country =? and education_level = ? and diploma_speciality = ? "
            + " ORDER BY ari.id DESC ";
	
    public AcademicRecognitionDB(DataSource ds) {
		super(ds);
	}
    public List<BGAcademicRecognitionExtendedImpl> getBGAcademicRecognitionExtendedRecords() throws SQLException {
        return selectRecords(SELECT_BG_ACADEMIC_RECOGNITION_RECORDS_EXTENDED, BGAcademicRecognitionExtendedImpl.class);
    }
    
    public List<BGAcademicRecognitionExtendedImpl> getBgAcademicRecognitionSimilarRecords(String applicant, String university,
    		String universityCountry, String oks, String speciality, Integer withoutId) throws SQLException{
    	
    	if(withoutId != null){
    		return selectRecords(SELECT_BG_ACADEMIC_RECOGNITION_SIMILAR_RECORDS_NOT_ID_EXTENDED, BGAcademicRecognitionExtendedImpl.class, withoutId,
    				applicant, university, universityCountry, oks, speciality);
    	} else {
    		return selectRecords(SELECT_BG_ACADEMIC_RECOGNITION_SIMILAR_RECORDS_EXTENDED, BGAcademicRecognitionExtendedImpl.class,
    				applicant, university, universityCountry, oks, speciality);
    	}
    }
    
    public static void main(String[] args) throws SQLException {
        StandAloneDataSource ds = new StandAloneDataSource();
        AcademicRecognitionDB db = new AcademicRecognitionDB(ds);
        System.out.println(db.getBGAcademicRecognitionExtendedRecords());
    }
}
