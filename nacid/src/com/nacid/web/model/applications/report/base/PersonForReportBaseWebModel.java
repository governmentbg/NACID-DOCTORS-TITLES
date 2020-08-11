package com.nacid.web.model.applications.report.base;

import com.nacid.bl.applications.base.PersonBase;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.data.DataConverter;

public class PersonForReportBaseWebModel {
	protected String id;
	protected String firstName;
	protected String surName;
	protected String lastName;
	protected String civilId;
	protected String civilIdType;
	protected String birthCity;
	protected String fullName;
	protected String birthDate;
	private String birthCountry;
	private String citizenship;
	
	private String citizenshipId;
	private String birthCountryId;
	private String civilIdTypeId;

	public PersonForReportBaseWebModel(PersonBase p) {
		id = p.getId() + "";
		firstName = p.getFName();
		surName = p.getSName();
		lastName = p.getLName();
		fullName = p.getFullName();
		civilId = p.getCivilId();
		birthCity = p.getBirthCity();
		this.birthDate = DataConverter.formatDate(p.getBirthDate(), "");
		FlatNomenclature civilIdType = p.getCivilIdType();
		this.civilIdType = civilIdType == null ? "" : civilIdType.getName();
		Country birthCountry = p.getBirthCountry();
		this.birthCountry = birthCountry == null ? "" : birthCountry.getName();
		Country citizenship = p.getCitizenship();
		this.citizenship = citizenship == null ? "" : citizenship.getName();
		
		
		citizenshipId = citizenship == null ? "" : citizenship.getId() + "";
		birthCountryId = birthCountry == null ? "" : birthCountry.getId() + "";
		civilIdTypeId = civilIdType == null ? "" : civilIdType.getId() + "";
	}

	public String getCitizenshipId() {
        return citizenshipId;
    }

    public String getBirthCountryId() {
        return birthCountryId;
    }

    public String getCivilIdTypeId() {
        return civilIdTypeId;
    }

    public String getBirthDate() {
		return birthDate;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSurName() {
		return surName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getCivilId() {
		return civilId;
	}

	public String getBirthCity() {
		return birthCity;
	}

	public String getCivilIdType() {
		return civilIdType;
	}

	public String getFullName() {
		return fullName;
	}

	public String getBirthCountry() {
		return birthCountry;
	}

	public String getCitizenship() {
		return citizenship;
	}
}
