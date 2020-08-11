package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Company;
import com.nacid.bl.nomenclatures.City;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.applications.CompanyRecord;

import java.util.Date;

public class CompanyImpl implements Company {

    private int id;
    private String name;
    private int countryId;
    private String cityName;
    private String pcode;
    private String addressDetails;
    private String phone;
    private Date dateFrom;
    private Date dateTo;
    private String eik;
    private Integer cityId;
    private NacidDataProvider nacidDataProvider;
    
    public CompanyImpl(NacidDataProvider nacidDataProvider, CompanyRecord rec) {
        
        this.id = rec.getId();
        this.name = rec.getName();
        this.countryId = rec.getCountryId();
        this.cityName = rec.getCity();
        this.pcode = rec.getPcode();
        this.addressDetails = rec.getAddressDetails();
        this.phone = rec.getPhone();
        this.dateFrom = rec.getDateFrom();
        this.dateTo = rec.getDateTo();
        this.eik = rec.getEik();
        this.cityId = rec.getCityId();

        this.nacidDataProvider = nacidDataProvider;
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCountryId() {
        return countryId;
    }
    public Country getCountry() {
    	return nacidDataProvider.getNomenclaturesDataProvider().getCountry(getCountryId());
    }

    @Override
    public String getCityName() {
        return cityName;
    }



    @Override
    public String getPcode() {
        return pcode;
    }

    @Override
    public String getAddressDetails() {
        return addressDetails;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public Date getDateFrom() {
        return dateFrom;
    }

    @Override
    public String getEik() {
        return eik;
    }

    @Override
    public Integer getCityId() {
        return cityId;
    }

    @Override
    public Date getDateTo() {
        return dateTo;
    }

    public City getCity() {
        return getCityId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getCity(getCityId());
    }
}
