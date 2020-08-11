package com.nacid.bl.impl.applications;

import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.applications.CompetentInstitutionRecord;

public class CompetentInstitutionImpl implements CompetentInstitution {
  private CompetentInstitutionRecord record;
  private NacidDataProvider nacidDataProvider;
  public CompetentInstitutionImpl(NacidDataProvider nacidDataProvider, CompetentInstitutionRecord record) {
    this.record = record;
    this.nacidDataProvider = nacidDataProvider;
  }
  public int getId() {
    return record.getId();
  }
  public int getCountryId() {
    return record.getCountryId();
  }
  public Country getCountry() {
    return nacidDataProvider.getNomenclaturesDataProvider().getCountry(getCountryId());
  }
  public String getName() {
    return record.getName();
  }
  public String getOriginalName() {
    return record.getOriginalName();
  }
  public String getPhone() {
    return record.getPhone();
  }
  public String getFax() {
    return record.getFax();
  }
  public String getEmail() {
    return record.getEmail();
  }
  public String getAddressDetails() {
    return record.getAddressDetails();
  }
  public String getUrl() {
    return record.getUrl();
  }
  public Date getDateFrom() {
    return record.getDateFrom();
  }
  public Date getDateTo() {
    return record.getDateTo();
  }
  public boolean isActive() {
	  return Utils.isRecordActive(getDateFrom(), getDateTo());
  }
}
