package com.nacid.bl.applications.base;

import java.util.Date;

import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;

public interface PersonBase {
	public int getId();
	public String getFName() ;
	public String getSName();
	public String getLName();
	public String getBirthCity();
	/**
	 * vry6ta ime + prezime + familiq
	 */
	public String getFullName();
	public Integer getCivilIdTypeId();
	public FlatNomenclature getCivilIdType();
	
	
	
	
	public String getCivilId();
	public Integer getBirthCountryId();
	public Country getBirthCountry();
	public Date getBirthDate();
	public Integer getCitizenshipId();
	public Country getCitizenship();
}
