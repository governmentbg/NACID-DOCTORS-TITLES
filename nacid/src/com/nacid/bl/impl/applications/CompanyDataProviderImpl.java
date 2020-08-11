package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Company;
import com.nacid.bl.applications.CompanyDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.CompanyRecord;
import com.nacid.db.applications.CompanyDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDataProviderImpl implements CompanyDataProvider {

    private CompanyDB db; 
    private NacidDataProvider nacidDataProvider;
    
    public CompanyDataProviderImpl(NacidDataProviderImpl nacidDataProviderImpl) {
        db = new CompanyDB(nacidDataProviderImpl.getDataSource());
        this.nacidDataProvider = nacidDataProviderImpl;
    }

    @Override
    public List<Company> getCompanies(boolean onlyActive) {
        
        List<CompanyRecord> records = null;
        try {
            records = db.selectRecords(CompanyRecord.class, " 1 = 1 ORDER BY NAME ");
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
        List<Company> ret = new ArrayList<Company>();
        if(records != null) {
            for(CompanyRecord rec : records) {
                ret.add(new CompanyImpl(nacidDataProvider, rec));
            }
        }
        return ret;
    }

    @Override
    public Company getCompany(int id) {
        CompanyRecord record = new CompanyRecord();
        try {
            record = db.selectRecord(record, id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
        if(record != null) {
            return new CompanyImpl(nacidDataProvider, record);
        }
        return null;
    }

    @Override
    public int saveCompany(int id, String name, int countryId, String city, 
            String pcode, String addressDetails, String phone,
            Date dateFrom, Date dateTo, String eik, Integer settlementId) {
        CompanyRecord rec = new CompanyRecord(id, name, countryId, 
                city, pcode, addressDetails, phone, 
                dateFrom != null ? new java.sql.Date(dateFrom.getTime()) : null,
                dateTo != null ? new java.sql.Date(dateTo.getTime()) : null,
                eik, settlementId);
        
        try {

            if (id <= 0) {
                rec = db.insertRecord(rec);
            } else {
                db.updateRecord(rec);
            }
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
        return rec.getId();
    }

    @Override
    public void disableCompany(int id) {
        
        try {
            db.setEndDateToToday(id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }

    @Override
    public List<Company> getCompaniesByPartOfEik(String partOfEik) {
        try {
            List<CompanyRecord> recs = db.selectRecords(CompanyRecord.class, "eik ilike ?", partOfEik + "%");
            return  recs.size() == 0 ? null : recs.stream().map(rec -> new CompanyImpl(nacidDataProvider, rec)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    public List<Company> getCompaniesByEik(String eik) {
        try {
            List<CompanyRecord> recs = db.selectRecords(CompanyRecord.class, "eik = ?", eik);
            return  recs.size() == 0 ? null : recs.stream().map(rec -> new CompanyImpl(nacidDataProvider, rec)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
    @Override
    public List<Company> getCompaniesByPartOfName(String partOfName, boolean startsWith) {
        try {
            List<CompanyRecord> recs = db.selectRecords(CompanyRecord.class, "name ilike ?", (startsWith ? "" : "%") + partOfName + "%");
            return  recs.size() == 0 ? null : recs.stream().map(rec -> new CompanyImpl(nacidDataProvider, rec)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }
}
