package com.nacid.bl.impl.external.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.ExtCompany;
import com.nacid.bl.external.applications.ExtCompanyDataProvider;
import com.nacid.bl.impl.NacidDataProviderImpl;
import com.nacid.bl.impl.Utils;
import com.nacid.data.applications.CompanyRecord;
import com.nacid.data.external.applications.ExtCompanyRecord;
import com.nacid.db.applications.CompanyDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ExtCompanyDataProviderImpl implements ExtCompanyDataProvider {

    private CompanyDB db;
    private NacidDataProvider nacidDataProvider;

    public ExtCompanyDataProviderImpl(NacidDataProviderImpl nacidDataProviderImpl) {
        db = new CompanyDB(nacidDataProviderImpl.getDataSource());
        this.nacidDataProvider = nacidDataProviderImpl;
    }

    @Override
    public List<ExtCompany> getCompanies(boolean onlyActive) {
        try {
            List<ExtCompanyRecord> records = db.selectRecords(ExtCompanyRecord.class, " 1 = 1 ORDER BY NAME ");
            List<ExtCompany> ret = new ArrayList<ExtCompany>();
            if(records != null) {
                for(ExtCompanyRecord rec : records) {
                    ret.add(new ExtCompanyImpl(nacidDataProvider, rec));
                }
            }
            return ret;
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        

    }

    @Override
    public ExtCompany getCompany(int id) {
        ExtCompanyRecord record = new ExtCompanyRecord();
        try {
            record = db.selectRecord(record, id);
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
        
        if(record != null) {
            return new ExtCompanyImpl(nacidDataProvider, record);
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
    public List<ExtCompany> getCompaniesByEik(String eik) {
        try {
            List<ExtCompanyRecord> result = db.selectRecords(ExtCompanyRecord.class, "eik = ?", eik);
            return result.size() == 0 ? null : result.stream().map(rec -> new ExtCompanyImpl(nacidDataProvider, rec)).collect(Collectors.toList());
        } catch (SQLException e) {
            throw Utils.logException(e);
        }
    }


}
