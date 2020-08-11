package com.nacid.bl.impl.applications;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.applications.CompetentInstitutionDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.CompetentInstitutionRecord;
import com.nacid.db.applications.CompetentInstitutionDB;
import com.nacid.db.utils.StandAloneDataSource;

public class CompetentInstitutionDataProviderImpl implements CompetentInstitutionDataProvider{
    private CompetentInstitutionDB db;
    private NacidDataProvider nacidDataProvider;
    public CompetentInstitutionDataProviderImpl(NacidDataProviderImpl nacidDataProvider) {
        this.db = new CompetentInstitutionDB(nacidDataProvider.getDataSource());
        this.nacidDataProvider = nacidDataProvider;
    }
    public void deleteCompetentInstitution(int institutionId) {
        try {
            CompetentInstitutionRecord record = db.selectRecord(new CompetentInstitutionRecord(), institutionId);
            if (record != null) {
                record.setDateTo(new java.sql.Date(new Date().getTime()));
                db.updateRecord(record);  
            }
        } catch (Exception e) {
            throw Utils.logException(e);
        }

    }

    public CompetentInstitution getCompetentInstitution(int institutionId) {
        try {
            CompetentInstitutionRecord record = db.selectRecord(new CompetentInstitutionRecord(), institutionId);
            if (record == null) {
                return null;
            }
            return new CompetentInstitutionImpl(nacidDataProvider, record);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


    public List<CompetentInstitution> getCompetentInstitutions(Integer countryId, boolean onlyActive) {
        try {
            List<CompetentInstitutionRecord> records = db.getCompetentInstitutionRecords(countryId, onlyActive ? Utils.getSqlDate(Utils.getToday()) : null);
            return toCompetentInstitutions(records, nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    public List<CompetentInstitution> getCompetentInstitutions(Collection<Integer> countryIds, boolean onlyActive) {
    	if (countryIds != null && countryIds.size() == 0) {
    		return null;
    	}
    	try {
            List<CompetentInstitutionRecord> records = db.getCompetentInstitutionRecords(countryIds, onlyActive ? Utils.getSqlDate(Utils.getToday()) : null);
            return toCompetentInstitutions(records, nacidDataProvider);
            
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
	}
   
    @Override
    public int saveCompetentInstitution(int id, int countryId, String name, String originalName, String phone, String fax, String email, String addressDetails,
            String url, String notes, Date dateFrom, Date dateTo) {
        CompetentInstitutionRecord record = new CompetentInstitutionRecord(id, countryId, name, originalName, phone, fax, email, addressDetails, url, notes, Utils.getSqlDate(dateFrom), Utils.getSqlDate(dateTo));
        try {
            if (id == 0) {
                record = db.insertRecord(record);
            } else {
                db.updateRecord(record);
            }
            return record.getId();
        } catch (Exception e) {
            throw Utils.logException(e);
        }
    }
    
    public List<CompetentInstitution> getCompetentInstitutionsByUniversityValidityId(int universityValidity, boolean onlyActive) {
        try {
            List<CompetentInstitutionRecord> records = db.getCompetentInstitutionRecordsByUniversityValidityId(universityValidity, onlyActive ? Utils.getSqlDate(Utils.getToday()) : null);
            return toCompetentInstitutions(records, nacidDataProvider);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    
    private static List<CompetentInstitution> toCompetentInstitutions(List<CompetentInstitutionRecord> records, NacidDataProvider nacidDataProvider) {
    	if (records.size() == 0) {
            return null;
        }
        List<CompetentInstitution> result = new ArrayList<CompetentInstitution>();
        for (CompetentInstitutionRecord r:records) {
            result.add(new CompetentInstitutionImpl(nacidDataProvider, r));
        }
        return result;	
    }
    
    public static void main(String[] args) {
        CompetentInstitutionDataProvider competentInstitutionDataProvider = NacidDataProvider.getNacidDataProvider(new StandAloneDataSource()).getCompetentInstitutionDataProvider();
        System.out.println(competentInstitutionDataProvider.getCompetentInstitutionsByUniversityValidityId(26, false).size());
    }
	
}
