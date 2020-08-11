package com.nacid.bl.impl.applications;

import java.util.Date;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.Person;
import com.nacid.bl.applications.PersonDocument;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.PersonDocumentRecord;
import com.nacid.data.applications.PersonRecord;

public class PersonImpl implements Person{
	private PersonRecord record;
	private PersonDocument personDocumentRecord;
	private NacidDataProvider nacidDataProvider;
	public PersonImpl(PersonRecord person, NacidDataProvider nacidDataProvider) {
		this.record = person;
		this.nacidDataProvider = nacidDataProvider;
	}
	public PersonDocument getPersonDocument() {
		if (personDocumentRecord == null) {
			synchronized (this) {
				if (personDocumentRecord == null) {
					personDocumentRecord = nacidDataProvider.getApplicationsDataProvider().getPersonDocument(record.getId());
				}
			}
		}
		return personDocumentRecord;
	}
	public String getBirthCity() {
		return record.getBirthCity();
	}

	public Integer getBirthCountryId() {
		return record.getBirthCountryId();
	}
	public Country getBirthCountry() {
		return getBirthCountryId() == null ? null :nacidDataProvider.getNomenclaturesDataProvider().getCountry(getBirthCountryId());
	}

	public Date getBirthDate() {
		return record.getBirthDate();
	}

	public Integer getCitizenshipId() {
		return record.getCitizenshipId();
	}
	public Country getCitizenship() {
		return getCitizenshipId() == null ? null :nacidDataProvider.getNomenclaturesDataProvider().getCountry(getCitizenshipId());
	}

	public String getCivilId() {
		return record.getCivilId();
	}

	public Integer getCivilIdTypeId() {
		return record.getCivilIdType();
	}
	public FlatNomenclature getCivilIdType() {
		return getCivilIdTypeId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE, getCivilIdTypeId());
	}

	public String getFName() {
		return record.getFName();
	}

	public String getFullName() {
		return record.getFullName();
	}

	public int getId() {
		return record.getId();
	}

	public String getLName() {
		return record.getLName();
	}

	public String getSName() {
		return record.getSName();
	}

}
