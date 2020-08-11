package com.nacid.web.handlers.impl.ajax;

import com.nacid.bl.applications.University;
import com.nacid.bl.applications.UniversityFaculty;
import com.nacid.web.model.applications.UniversityWebModel;

/**
 * Created by georgi.georgiev on 24.06.2015.
 */
public class DiplomaTypeUniversityWebModel {
    private int id;
    private String bgName;
    private String orgName;
    private Integer countryId;
    private String address;
    private String city;
    private String facultyId;
    private String facultyName;
    private String genericName;
    private String genericNameId;
    private String urlDiplomaRegister;
    private String urlDiplomaRegisterLink;


    public DiplomaTypeUniversityWebModel(University u, UniversityFaculty uf) {
        this.id = u.getId();
        this.bgName = u.getBgName();
        this.orgName = u.getOrgName() == null ? "" : u.getOrgName();
        this.countryId = u.getCountryId();
        this.city = u.getCity() == null ? "" : u.getCity();
        this.address = u.getAddrDetails() == null ? "" : u.getAddrDetails();
        this.facultyId = uf == null ? "" : uf.getId() + "";
        this.facultyName = uf == null ? "" : uf.getName();
        this.genericName = u.getGenericName() == null ? "" : u.getGenericName().getName();
        this.genericNameId = u.getGenericNameId() == null ? "" : u.getGenericNameId() + "";
        this.urlDiplomaRegister = u.getUrlDiplomaRegister() == null ? "" : u.getUrlDiplomaRegister();
        this.urlDiplomaRegisterLink = UniversityWebModel.getUrlDiplomaRegisterLink(u.getUrlDiplomaRegister());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBgName() {
        return bgName;
    }

    public void setBgName(String bgName) {
        this.bgName = bgName;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGenericName() {
        return genericName;
    }

    public String getGenericNameId() {
        return genericNameId;
    }

    public String getUrlDiplomaRegister() {
        return urlDiplomaRegister;
    }

    public String getUrlDiplomaRegisterLink() {
        return urlDiplomaRegisterLink;
    }
}
