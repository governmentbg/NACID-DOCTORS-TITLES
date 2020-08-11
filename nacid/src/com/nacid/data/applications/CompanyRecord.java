package com.nacid.data.applications;

import java.sql.Date;

public class CompanyRecord {

    private int id;
    private String name;
    private int countryId;
    private String city;
    private String pcode;
    private String addressDetails;
    private String phone;
    private Date dateFrom;
    private Date dateTo;
    private String eik;
    private Integer cityId;
    
    public CompanyRecord() {
        
    }
    
    public CompanyRecord(int id, String name, int countryId, String city, 
            String pcode, String addressDetails, String phone,
            Date dateFrom, Date dateTo, String eik, Integer cityId) {
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.city = city;
        this.pcode = pcode;
        this.addressDetails = addressDetails;
        this.phone = phone;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.eik = eik;
        this.cityId = cityId;
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

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
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

    public String getEik() {
        return eik;
    }

    public void setEik(String eik) {
        this.eik = eik;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
