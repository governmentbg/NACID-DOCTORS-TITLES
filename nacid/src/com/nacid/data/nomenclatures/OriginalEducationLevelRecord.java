package com.nacid.data.nomenclatures;

import java.sql.Date;

/**
 * Created by georgi.georgiev on 30.04.2015.
 */
public class OriginalEducationLevelRecord extends FlatNomenclatureRecord {
    private int countryId;
    private int eduLevelId;
    private String nameTranslated;

    public OriginalEducationLevelRecord() {
    }

    public OriginalEducationLevelRecord(int id, String name, String nameTranslated, Date dateFrom, Date dateTo, int countryId, int eduLevelId) {
        super(id, name, dateFrom, dateTo);
        this.countryId = countryId;
        this.eduLevelId = eduLevelId;
        this.nameTranslated = nameTranslated;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getEduLevelId() {
        return eduLevelId;
    }

    public void setEduLevelId(int eduLevelId) {
        this.eduLevelId = eduLevelId;
    }

    public String getNameTranslated() {
        return nameTranslated;
    }

    public void setNameTranslated(String nameTranslated) {
        this.nameTranslated = nameTranslated;
    }
}
