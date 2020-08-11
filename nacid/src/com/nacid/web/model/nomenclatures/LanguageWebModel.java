package com.nacid.web.model.nomenclatures;

import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.Language;
import com.nacid.data.DataConverter;

import java.util.Date;

public class LanguageWebModel {
    private String id = "";
    private String name = "";
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private String iso639Code = "";

    public LanguageWebModel(int id, String name, String iso639Code, String dateFrom, String dateTo) {
        this.id = id + "";
        this.name = name;
        this.iso639Code = iso639Code;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
    public LanguageWebModel(Language country) {
        this.id = country.getId() + "";
        this.name = country.getName();
        this.iso639Code = country.getIso639Code();
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

    public String getIso639Code() {
        return iso639Code;
    }
}
