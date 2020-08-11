package com.nacid.bl.applications;

import java.util.Date;

import com.nacid.bl.nomenclatures.Country;

public interface CompetentInstitution {
  public int getId();
  public int getCountryId() ;
  public Country getCountry();
  public String getName();
  public String getOriginalName();
  public String getPhone();
  public String getFax();
  public String getEmail();
  public String getAddressDetails();
  public String getUrl();
  public Date getDateFrom();
  public Date getDateTo();
  public boolean isActive();
}
