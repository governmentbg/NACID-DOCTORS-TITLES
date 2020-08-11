package com.nacid.bl.applications;

import java.util.Date;

import com.nacid.bl.impl.nomenclatures.UniversityGenericNameImpl;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;

public interface University {

	public int getId();
	public int getCountryId();
	public Country getCountry();
	public String getBgName();
	public String getOrgName();
	public String getCity();
	public String getAddrDetails();
	public String getPhone();
	public String getFax();
	public String getEmail();
	public String getWebSite();
	public String getUrlDiplomaRegister();
	public Date getDateFrom();
	public Date getDateTo();
	public boolean isActive();
	public Integer getGenericNameId();
	public FlatNomenclature getGenericName();
}
