package com.ext.nacid.web.model.applications;

import com.nacid.bl.external.ExtAddress;

/**
 * Created by georgi.georgiev on 18.09.2015.
 */
public class ExtAddressWebModel {
    private String id;
    private String email;
    private String address;
    private String postalCode;
    private String phone;
    private String fax;
    private String countryName;
    private String cityName;
    private String foreignCity;
    private String postalBox;


    public ExtAddressWebModel(ExtAddress addr) {

        this.id = addr.getId() + "";
        this.email = addr.getEmail();
        this.address = addr.getAddress();
        this.postalCode = addr.getPostalCode();
        this.phone = addr.getPhone();
        this.fax = addr.getFax();
        this.countryName = addr.getCountryId() == null ? "" : addr.getCountry().getName();
        this.cityName = addr.getCityId() == null ? "" : addr.getCity().getName();
        this.foreignCity = addr.getForeignCity();
        this.postalCode = addr.getPostalCode();
        this.postalBox = addr.getPostalBox();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getForeignCity() {
        return foreignCity;
    }

    public String getPostalBox() {
        return postalBox;
    }
}
