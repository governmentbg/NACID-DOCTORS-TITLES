package com.nacid.bl.impl.applications;

import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.University;
import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.UniversityRecord;

public class UniversityImpl implements University {

	private int id;
	private int countryId;
	private String bgName;
	private String orgName;
	private String city;
	private String addrDetails;
	private String phone;
	private String fax;
	private String email;
	private String webSite;
	private String urlDiplomaRegister;
	private Date dateFrom;
	private Date dateTo;
	private Integer universityGenericNameId;
	private NacidDataProvider nacidDataProvider;
	
	public UniversityImpl(NacidDataProvider nacidDataProvider, UniversityRecord record) {
		id = record.getId();
		countryId = record.getCountryId();
		bgName = record.getBgName();
		orgName = record.getOrgName();
		city = record.getCity();
		addrDetails = record.getAddrDetails();
		phone = record.getPhone();
		fax = record.getFax();
		email = record.getEmail();
		webSite = record.getWebSite();
		urlDiplomaRegister = record.getUrlDiplomaRegister();
		dateFrom = (Date)record.getDateFrom();
		dateTo = (Date)record.getDateTo();
		this.nacidDataProvider = nacidDataProvider;
		this.universityGenericNameId = record.getUniversityGenericNameId();
	}
	
	@Override
	public String getAddrDetails() {
		return addrDetails;
	}

	@Override
	public String getBgName() {
		return bgName;
	}

	@Override
	public String getCity() {
		return city;
	}

	@Override
	public int getCountryId() {
		return countryId;
	}
	public Country getCountry() {
		return nacidDataProvider.getNomenclaturesDataProvider().getCountry(getCountryId());
	}

	@Override
	public Date getDateFrom() {
		return dateFrom;
	}

	@Override
	public Date getDateTo() {
		return dateTo;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getOrgName() {
		return orgName;
	}

	@Override
	public String getPhone() {
		return phone;
	}

	@Override
	public String getUrlDiplomaRegister() {
		return urlDiplomaRegister;
	}

	@Override
	public String getWebSite() {
		return webSite;
	}

	@Override
	public boolean isActive() {
	    return Utils.isRecordActive(dateFrom, dateTo);
	}

	@Override
	public Integer getGenericNameId() {
		return universityGenericNameId;
	}

	@Override
	public FlatNomenclature getGenericName() {
		return universityGenericNameId == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_UNIVERSITY_GENERIC_NAME, universityGenericNameId);
	}
}
