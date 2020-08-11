package com.nacid.data.applications;

import com.nacid.bl.applications.SimilarDiploma;

import java.util.Date;
import java.util.List;

/**
 * Created by georgi.georgiev on 14.10.2015.
 */
public class SimilarDiplomaRecord implements SimilarDiploma {
    private int id;
    private Date diplomaDate;
    private String eduLevelName;
    private String appNum;
    private Date appDate;
    private int civilIdType;
    private String civilId;
    private String fname;
    private String sname;
    private String lname;
    private List<String> specialityNames;
    private List<String> universityNames;
    private List<String> universityCountryNames;
    private List<Integer> universityCountryIds;
    private Integer eduLevelId;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDiplomaDate() {
        return diplomaDate;
    }

    public void setDiplomaDate(Date diplomaDate) {
        this.diplomaDate = diplomaDate;
    }

    public String getEduLevelName() {
        return eduLevelName;
    }

    public void setEduLevelName(String eduLevelName) {
        this.eduLevelName = eduLevelName;
    }

    public String getAppNum() {
        return appNum;
    }

    public void setAppNum(String appNum) {
        this.appNum = appNum;
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public int getCivilIdType() {
        return civilIdType;
    }

    public void setCivilIdType(int civilIdType) {
        this.civilIdType = civilIdType;
    }

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public List<String> getSpecialityNames() {
        return specialityNames;
    }

    public void setSpecialityNames(List<String> specialityNames) {
        this.specialityNames = specialityNames;
    }

    public List<String> getUniversityNames() {
        return universityNames;
    }

    public void setUniversityNames(List<String> universityNames) {
        this.universityNames = universityNames;
    }

    public List<String> getUniversityCountryNames() {
        return universityCountryNames;
    }

    public void setUniversityCountryNames(List<String> universityCountryNames) {
        this.universityCountryNames = universityCountryNames;
    }

    public List<Integer> getUniversityCountryIds() {
        return universityCountryIds;
    }

    public void setUniversityCountryIds(List<Integer> universityCountryIds) {
        this.universityCountryIds = universityCountryIds;
    }

    public Integer getEduLevelId() {
        return eduLevelId;
    }

    public void setEduLevelId(Integer eduLevelId) {
        this.eduLevelId = eduLevelId;
    }
}
