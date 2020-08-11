package com.nacid.db.external.applications;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.nacid.data.external.applications.ExtDiplomaIssuerRecord;
import com.nacid.data.external.applications.ExtTrainingCourseSpecialityRecord;
import com.nacid.data.external.applications.ExtTrainingCourseTrainingLocationRecord;
import com.nacid.db.utils.DatabaseService;

public class ExtTrainingCourseDB extends DatabaseService {

    public ExtTrainingCourseDB(DataSource ds) {
        super(ds);    
    }
    public List<ExtTrainingCourseTrainingLocationRecord> getTrainingCourseTrainingLocationRecords(int trainingCourseId) throws SQLException {
    	return selectRecords(ExtTrainingCourseTrainingLocationRecord.class, " training_course_id = ? ", trainingCourseId);
    }
    public void deleteTrainingCourseTrainingLocationRecords(int trainingCourseId) throws SQLException {
    	deleteRecords(ExtTrainingCourseTrainingLocationRecord.class, " training_course_id = ? ", trainingCourseId);
    }
    public List<ExtDiplomaIssuerRecord> getDiplomaIssuers(int trainingCourseId) throws SQLException {
    	return selectRecords(ExtDiplomaIssuerRecord.class, "diploma_id = ? order by ord_num", trainingCourseId);
    }
    public void deleteDiplomaIssuerRecords(int trainingCourseId) throws SQLException {
    	deleteRecords(ExtDiplomaIssuerRecord.class, " diploma_id = ? ", trainingCourseId);
    }
    public List<ExtTrainingCourseSpecialityRecord> getExtTrainingCourseSpecialities(int trainingCourseId) throws SQLException {
        return selectRecords(ExtTrainingCourseSpecialityRecord.class, " ext_training_course_id = ? ", trainingCourseId);
    }

}
