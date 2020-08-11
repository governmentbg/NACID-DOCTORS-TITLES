package com.nacid.web.model.applications.report.base;

import com.nacid.bl.applications.base.CompanyBase;
import com.nacid.data.DataConverter;
import com.nacid.web.model.nomenclatures.CityWebModel;

/**
 * Created by georgi.georgiev on 16.09.2015.
 */
public class CompanyForReportBaseWebModel {
    private String id;
    private String name;
    private String countryName;
    private String cityName;
    private String postCode;
    private String addressDetails;
    private String phone;
    private String dateFrom;
    private String dateTo;
    private String eik;
    private CityWebModel city;
    private int countryId;

    public CompanyForReportBaseWebModel(CompanyBase c) {
        this.id = c.getId() + "";
        this.name = c.getName();
        this.countryName = c.getCountry().getName();
        this.countryId = c.getCountryId();
        this.cityName = c.getCityName();
        this.postCode = c.getPcode();
        this.addressDetails = c.getAddressDetails();
        this.phone = c.getPhone();
        this.dateFrom = DataConverter.formatDate(c.getDateFrom());
        this.dateTo = DataConverter.formatDate(c.getDateTo());
        this.eik = c.getEik();
        this.city = c.getCityId() == null ? null : new CityWebModel(c.getCity());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public String getPhone() {
        return phone;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getEik() {
        return eik;
    }

    public CityWebModel getCity() {
        return city;
    }

    public int getCountryId() {
        return countryId;
    }
}
