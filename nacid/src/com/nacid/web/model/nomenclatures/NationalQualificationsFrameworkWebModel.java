package com.nacid.web.model.nomenclatures;

import com.nacid.bl.nomenclatures.NationalQualificationsFramework;
import com.nacid.data.DataConverter;

import java.util.Date;

public class NationalQualificationsFrameworkWebModel {
    private String id = "";
    private String name = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private int countryId;


    public NationalQualificationsFrameworkWebModel(int id, String name, String dateFrom, String dateTo, int countryId) {
        this.id = id + "";
        this.name = name;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.countryId = countryId;
    }
    public NationalQualificationsFrameworkWebModel(NationalQualificationsFramework nqf) {
        this.id = nqf.getId() + "";
        this.name = nqf.getName();
        this.dateFrom = nqf.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(nqf.getDateFrom());
        this.dateTo = nqf.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(nqf.getDateTo());
        this.countryId = nqf.getCountryId();
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

    public int getCountryId() {
        return countryId;
    }

}
