package com.nacid.data.applications;

import java.sql.Date;

public class TrainingInstitutionRecord {

    private int id;
    private String name;
    private int countryId;
    private String city;
    private String pcode;
    private String addrDetails;
    private String phone;
    private Date dateFrom;
    private Date dateTo;
    private int[] univIds;
    
    public TrainingInstitutionRecord() {
        
    }

    public TrainingInstitutionRecord(int id, String name, int countryId, String city, String pcode, String addrDetails, String phone, Date dateFrom,
            Date dateTo, int[] univIds) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.city = city;
        this.pcode = pcode;
        this.addrDetails = addrDetails;
        this.phone = phone;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.univIds = univIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getAddrDetails() {
        return addrDetails;
    }

    public void setAddrDetails(String addrDetails) {
        this.addrDetails = addrDetails;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int[] getUnivIds() {
        return univIds;
    }

    public void setUnivIds(int[] univIds) {
        this.univIds = univIds;
    }
    
    
    
}
