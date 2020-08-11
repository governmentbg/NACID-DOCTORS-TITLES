package com.nacid.web.model.applications.base;

import com.nacid.bl.applications.base.DocumentRecipientBase;

/**
 * User: ggeorgiev
 * Date: 8.10.2019 г.
 * Time: 14:55
 */
public abstract class DocumentRecipientBaseWebModel {
    private String name;
    private String city;
    private String district;
    private String postCode;
    private String address;
    private String mobilePhone;
    private String country;

    public DocumentRecipientBaseWebModel(DocumentRecipientBase base) {
        this.name = base.getName();
        this.city = base.getCity();
        this.district = base.getDistrict();
        this.postCode = base.getPostCode();
        this.address = base.getAddress();
        this.mobilePhone = base.getMobilePhone();
        this.country = base.getCountry() == null ? null : base.getCountry().getName();
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getAddress() {
        return address;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getCountry() {
        return country;
    }
}
