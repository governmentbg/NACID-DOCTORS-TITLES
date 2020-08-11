package com.nacid.data.external.applications;

import com.nacid.data.applications.CompanyRecord;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
public class ExtCompanyRecord extends CompanyRecord {
    public ExtCompanyRecord() {
    }

    public ExtCompanyRecord(int id, String name, int countryId, String city, String pcode, String addressDetails, String phone, Date dateFrom, Date dateTo, String eik, Integer cityId) {
        super(id, name, countryId, city, pcode, addressDetails, phone, dateFrom, dateTo, eik, cityId);
    }
}
