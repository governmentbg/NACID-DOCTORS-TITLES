package com.nacid.data.applications;

import java.sql.Date;
import java.util.List;

/**
 * Created by georgi.georgiev on 19.05.2015.
 */
public class DiplomaTypeRecordForList {
    private int id;
    private String title;
    private String eduLevelName;
    private int eduLevelId;
    private Date dateFrom;
    private Date dateTo;
    private List<String> uniNames;
    private List<String> uniCountries;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEduLevelName() {
        return eduLevelName;
    }

    public void setEduLevelName(String eduLevelName) {
        this.eduLevelName = eduLevelName;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public List<String> getUniNames() {
        return uniNames;
    }

    public void setUniNames(List<String> uniNames) {
        this.uniNames = uniNames;
    }

    public List<String> getUniCountries() {
        return uniCountries;
    }

    public void setUniCountries(List<String> uniCountries) {
        this.uniCountries = uniCountries;
    }

    public int getEduLevelId() {
        return eduLevelId;
    }

    public void setEduLevelId(int eduLevelId) {
        this.eduLevelId = eduLevelId;
    }
    /*public String getUniNamesJoined() {
        return StringUtils.join(uniNames, ",<br /> ");
    }
    public String getCountryName() {
        return uniCountries == null || uniCountries.size() == 0 ? "" : (uniCountries.size() == 1 ? uniCountries.get(0) : "съвместна образователна степен");
    }*/
}
