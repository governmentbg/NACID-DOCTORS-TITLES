package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 04.08.2015.
 */
public class NationalQualificationsFrameworkRecord extends FlatNomenclatureRecord {
    private int countryId;
    public NationalQualificationsFrameworkRecord() {
    }

    public NationalQualificationsFrameworkRecord(int id, String name, int countryId, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
