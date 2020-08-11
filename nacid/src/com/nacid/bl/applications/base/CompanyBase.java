package com.nacid.bl.applications.base;

import com.nacid.bl.nomenclatures.City;
import com.nacid.bl.nomenclatures.Country;

import java.util.Date;

public interface CompanyBase {

    public int getId();
    public String getName();
    public int getCountryId();
    public Country getCountry();
    public String getCityName();
    public String getPcode();
    public String getAddressDetails();
    public String getPhone();
    public Date getDateTo();
    public Date getDateFrom();
    public String getEik();
    public Integer getCityId();
    public City getCity();

    
}
