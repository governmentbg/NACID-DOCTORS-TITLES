package com.nacid.data.nomenclatures;

import java.sql.Date;

public class CountryRecord extends FlatNomenclatureRecord{
    private String iso3166Code;
    private String officialName;
    public CountryRecord() {

    }
    public CountryRecord(int id, String name, String iso3166Code, String officialName, Date dateFrom, Date dateTo) {
        super(id, name, dateFrom, dateTo);
        this.iso3166Code = iso3166Code;
        this.officialName = officialName;
    }
    public String getIso3166Code() {
        return iso3166Code;
    }
    public void setIso3166Code(String iso3166Code) {
        this.iso3166Code = iso3166Code;
    }
    public String getOfficialName() {
        return officialName;
    }
    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }
}