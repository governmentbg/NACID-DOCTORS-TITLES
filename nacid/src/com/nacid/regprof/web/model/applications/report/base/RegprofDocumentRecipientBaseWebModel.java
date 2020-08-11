package com.nacid.regprof.web.model.applications.report.base;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.DocumentRecipientRecord;
import com.nacid.data.regprof.applications.RegprofDocumentRecipientRecord;

/**
 * User: ggeorgiev
 * Date: 9.10.2019 г.
 * Time: 16:05
 */
public class RegprofDocumentRecipientBaseWebModel {
    private String name;
    private String city;
    private String district;
    private String postCode;
    private String address;
    private String mobilePhone;
    private String country;
    public RegprofDocumentRecipientBaseWebModel(NomenclaturesDataProvider nomenclaturesDataProvider, DocumentRecipientRecord rec) {
        this.name = rec.getName();
        this.city = rec.getCity();
        this.district = rec.getDistrict();
        this.postCode = rec.getPostCode();
        this.address = rec.getAddress();
        this.mobilePhone = rec.getMobilePhone();
        this.country = nomenclaturesDataProvider.getCountry(rec.getCountryId()).getName();
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
