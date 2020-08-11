package com.nacid.bl.applications;

import java.sql.Date;

import com.nacid.bl.nomenclatures.Country;

public interface TrainingInstitution {

    public int getId();

    public String getName();

    public int getCountryId();
    
    public Country getCountry();

    public String getCity();

    public String getPcode();

    public String getAddrDetails();

    public String getPhone();

    public Date getDateFrom();

    public Date getDateTo();

    public int[] getUnivIds();
    
    public boolean isActive();

}
