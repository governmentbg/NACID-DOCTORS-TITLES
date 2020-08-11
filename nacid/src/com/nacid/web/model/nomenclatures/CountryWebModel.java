package com.nacid.web.model.nomenclatures;

import java.util.Date;

import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.DataConverter;

public class CountryWebModel {
    private String id = "";
    private String name = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private String iso3166Code = "";
    private String officialName = "";

    public CountryWebModel(int id, String name, String iso3166Code, String officialName, String dateFrom, String dateTo) {
        this.id = id + "";
        this.name = name;
        this.iso3166Code = iso3166Code;
        this.officialName = officialName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
    public CountryWebModel(Country country) {
        this.id = country.getId() + "";
        this.name = country.getName();
        this.iso3166Code = country.getIso3166Code();
        this.officialName = country.getOfficialName();
        this.dateFrom = country.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(country.getDateFrom());
        this.dateTo = country.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(country.getDateTo());
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDateFrom() {
        return dateFrom;
    }
    public String getDateTo() {
        return dateTo;
    }
    public String getIso3166Code() {
        return iso3166Code;
    }
    public String getOfficialName() {
        return officialName;
    }
}
