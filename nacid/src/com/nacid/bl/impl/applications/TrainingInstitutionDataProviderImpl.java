package com.nacid.bl.impl.applications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingInstitution;
import com.nacid.bl.applications.TrainingInstitutionDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.TrainingInstitutionRecord;
import com.nacid.db.applications.TrainingInstitutionDB;

public class TrainingInstitutionDataProviderImpl implements TrainingInstitutionDataProvider {

    private TrainingInstitutionDB db;
    private NacidDataProvider nacidDataProvider;
    public TrainingInstitutionDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new TrainingInstitutionDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    
    @Override
    public void deactivateTrainingInstitution(int trainingInstId) {
        try {
            db.deactivateTrainingInstitution(trainingInstId);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public int saveTrainingInstitution(int id, String name, int countryId, String city, String pcode, String addrDetails, String phone,
    		java.util.Date dateFrom, java.util.Date dateTo, int[] univIds) {
        try {
            TrainingInstitutionRecord rec = new TrainingInstitutionRecord(id, 
                    name, countryId, city, pcode, addrDetails, phone,
                    Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo), univIds);
            
            return db.saveTrainingInstitution(rec);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public TrainingInstitution selectTrainingInstitution(int trainingInstId) {
        try {
            TrainingInstitutionRecord rec = db.selectTrainingInstitution(trainingInstId);
            if(rec == null) {
                return null;
            }
            return new TrainingInstitutionImpl(rec, nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<TrainingInstitution> selectTrainingInstitutionsByCountry(int countryId) {
        try {
            return generateTrainingInstitutionsFromRecords(db.selectTrainingInstitutionsByCountry(countryId), nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public List<TrainingInstitution> selectTrainingInstitutionsByUniversities(List<Integer> universityIds) {
    	if (universityIds != null && universityIds.size() == 0) {
    		return null;
    	}
    	try {
            return generateTrainingInstitutionsFromRecords(db.selectTrainingInstitutionsByUniversities(universityIds), nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    @Override
    public List<TrainingInstitution> selectTrainingInstitutions() {
        try {
            return generateTrainingInstitutionsFromRecords(db.selectTrainingInstitutions(), nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    private static List<TrainingInstitution> generateTrainingInstitutionsFromRecords(List<TrainingInstitutionRecord> records, NacidDataProvider nacidDataProvider) {
    	if (records == null || records.size() == 0) {
    		return null;
    	}
    	List<TrainingInstitution> ret = new ArrayList<TrainingInstitution>();
		for (TrainingInstitutionRecord rec : records) {
			ret.add(new TrainingInstitutionImpl(rec, nacidDataProvider));
		}
        return ret;
    }

    @Override
    public List<Integer> getInstitutionIds(Integer countryId, boolean startsWith, String partOfName) {
        try {
            List<Integer> res = db.getTrainingInstitutionRecords(countryId, startsWith, partOfName, false).stream().map(r -> r.getId()).collect(Collectors.toList());
            return res.size() == 0 ? null : res;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
}
