package com.nacid.db.applications;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;

import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.TrainingInstitutionRecord;
import com.nacid.data.applications.UniversityIdRecord;
import com.nacid.db.utils.DatabaseService;
import com.nacid.db.utils.SQLUtils;
import com.nacid.db.utils.StandAloneDataSource;

public class TrainingInstitutionDB extends DatabaseService {

    public TrainingInstitutionDB(DataSource ds) {
        super(ds);
    }
    
    @Override
    public <T> T insertRecord(T o) throws SQLException {
        throw new RuntimeException("Method not supported");
    };
    @Override
    public void updateRecord(Object o) throws SQLException {
        throw new RuntimeException("Method not supported");
    }
    @Override
    public <T> void deleteRecord(Class<T> cls, Object uniqueColumnValue) throws SQLException {
        throw new RuntimeException("Method not supported");
    }
    @Override
    public <T> void deleteRecords(Class<T> cls, String condition, Object... parameters) throws SQLException {
        throw new RuntimeException("Method not supported");
    }
    @Override
    public <T> T selectRecord(T object, Object uniqueColumnValue) throws SQLException {
        throw new RuntimeException("Method not supported");
    };
    @Override
    public <T> List<T> selectRecords(Class<T> cls, String condition, Object... parameters) throws SQLException {
        throw new RuntimeException("Method not supported");
    }
    
    
    /*public List<TrainingInstitutionRecord> selectTrainingInstitutionsByUniversity(int universityId) throws SQLException {
        return super.selectRecords(TrainingInstitutionRecord.class, 
                "id in (select training_inst_id from university_id where university_id=?)",
                universityId);
    }*/
    
    public List<TrainingInstitutionRecord> selectTrainingInstitutionsByUniversities(List<Integer> universityIds) throws SQLException {
        
    	List<Object> objects = new ArrayList<Object>();
    	String str;
        if (universityIds != null) {
        	objects.addAll(universityIds);
        	str = " id in (select training_inst_id from university_id where university_id in (" + SQLUtils.columnsToParameterList(StringUtils.join(universityIds, ",")) + ") ) ";
        } else {
        	str = " 1 = 1 ";
        }
    	return super.selectRecords(TrainingInstitutionRecord.class, str, objects.toArray());
    }
    public List<TrainingInstitutionRecord> selectTrainingInstitutionsByCountry(int countryId) throws SQLException {
    	return super.selectRecords(TrainingInstitutionRecord.class, "country_id = ?", countryId);
    }
    
    public List<TrainingInstitutionRecord> selectTrainingInstitutions() throws SQLException {
        return super.selectRecords(TrainingInstitutionRecord.class, null);
    }
    
    public TrainingInstitutionRecord selectTrainingInstitution(int trainingInstId) throws SQLException {
        TrainingInstitutionRecord rec = new TrainingInstitutionRecord();
        rec = super.selectRecord(rec, trainingInstId);
        List<UniversityIdRecord> univs = super.selectRecords(UniversityIdRecord.class, 
                "training_inst_id=?", rec.getId());
        if(univs != null && univs.size() > 0) {
            int[] univIds = new int[univs.size()];
            for(int i = 0; i < univIds.length; i++) {
                univIds[i] = univs.get(i).getUniversityId();
            }
            rec.setUnivIds(univIds);
        }
        
        return rec;
    }

    public List<TrainingInstitutionRecord> getTrainingInstitutionRecords(Integer countryId, boolean startsWith, String partOfName, boolean onlyActive) throws SQLException {
        List<Object> params = new ArrayList<>();
        String sql;
        sql = " name ilike ?";
        params.add((startsWith ? "" : "%") + partOfName + "%");

        if (onlyActive) {
            sql += " AND (date_from <= ? OR date_from is null) "
                    + " AND (date_to >= ? OR date_to is null) " ;
            Date d = new Date(System.currentTimeMillis());
            params.add(d);
            params.add(d);
        }

        if (countryId != null) {
            sql += " AND country_id = ?";
            params.add(countryId);
        }
        return super.selectRecords(TrainingInstitutionRecord.class, sql, params.toArray());
    }
    
    public int saveTrainingInstitution(TrainingInstitutionRecord trInstRec) throws SQLException {
        
        if(trInstRec.getId() == 0) {
            if(trInstRec.getDateFrom() == null) {
                trInstRec.setDateFrom(Utils.getSqlDate(Utils.getToday()));
            }
            trInstRec = super.insertRecord(trInstRec);
        }
        else {
            super.deleteRecords(UniversityIdRecord.class, 
                    "training_inst_id=?", trInstRec.getId());
            super.updateRecord(trInstRec);
        }
        if (trInstRec.getUnivIds() != null) {
            for (int i : trInstRec.getUnivIds()) {
                UniversityIdRecord uid = new UniversityIdRecord();
                uid.setTrainingInstId(trInstRec.getId());
                uid.setUniversityId(i);
                super.insertRecord(uid);
            }
        }
        return trInstRec.getId();
    }
    
    public void deactivateTrainingInstitution(int trainingInstId) throws SQLException {
        TrainingInstitutionRecord rec = new TrainingInstitutionRecord();
        rec = super.selectRecord(rec, trainingInstId);
        rec.setDateTo(new Date(System.currentTimeMillis()));
        super.updateRecord(rec);
    }
    public static void main(String[] args) throws SQLException {
    	TrainingInstitutionDB db = new TrainingInstitutionDB(new StandAloneDataSource());
    	System.out.println(db.selectTrainingInstitutionsByUniversities(Arrays.asList(19,21)).size());
    }
}
