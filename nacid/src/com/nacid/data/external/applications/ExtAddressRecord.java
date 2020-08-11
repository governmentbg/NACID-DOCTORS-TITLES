package com.nacid.data.external.applications;

import com.nacid.data.annotations.Column;
import com.nacid.data.annotations.Table;

/**
 * Created by georgi.georgiev on 18.09.2015.
 */
@Table(name="eservices.address", sequence = "eservices.address_id_seq")
public class ExtAddressRecord {
    private int id;
    private String email;
    private String address;
    @Column(name="postalcode")
    private String postalCode;
    private String phone;
    private String fax;
    private Integer countryId;
    @Column(name="settlement_id")
    private Integer cityId;
    private String foreignCity;
    @Column(name="postalbox")
    private String postalBox;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    
    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    
    public String getForeignCity() {
        return foreignCity;
    }

    public void setForeignCity(String foreignCity) {
        this.foreignCity = foreignCity;
    }

    
    public String getPostalBox() {
        return postalBox;
    }

    public void setPostalBox(String postalBox) {
        this.postalBox = postalBox;
    }
}
