package com.nacid.web.model.nomenclatures;

import com.nacid.bl.nomenclatures.OriginalEducationLevel;
import com.nacid.data.DataConverter;

import java.util.Date;

public class OriginalEducationLevelWebModel {
    private String id = "";
    private String name = "";
    private String nameTranslated;
    private String dateFrom = DataConverter.formatDate(new Date());
    private String dateTo = "дд.мм.гггг";
    private int countryId;
    private int eduLevelId;

    public OriginalEducationLevelWebModel(int id, String name, String nameTranslated, String dateFrom, String dateTo, int countryId, int eduLevelId) {
        this.id = id + "";
        this.name = name;
        this.nameTranslated = nameTranslated;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.countryId = countryId;
        this.eduLevelId = eduLevelId;
    }
    public OriginalEducationLevelWebModel(OriginalEducationLevel educationLevel) {
        this.id = educationLevel.getId() + "";
        this.name = educationLevel.getName();
        this.nameTranslated = educationLevel.getNameTranslated();
        this.dateFrom = educationLevel.getDateFrom() == null ? "дд.мм.гггг" : DataConverter.formatDate(educationLevel.getDateFrom());
        this.dateTo = educationLevel.getDateTo() == null ? "дд.мм.гггг" : DataConverter.formatDate(educationLevel.getDateTo());
        this.countryId = educationLevel.getCountryId();
        this.eduLevelId = educationLevel.getEducationLevelId();
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

    public int getEduLevelId() {
        return eduLevelId;
    }

    public String getNameTranslated() {
        return nameTranslated;
    }
}
