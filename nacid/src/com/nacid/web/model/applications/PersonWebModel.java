package com.nacid.web.model.applications;

import com.nacid.bl.applications.Person;
import com.nacid.data.DataConverter;

public class PersonWebModel {
	protected String id;
	protected String firstName;
	protected String surName;
	protected String lastName;
	protected String civilId;
	protected String civilIdType;
	protected String birthCity;
	protected String fullName;
	protected String birthDate;

	public PersonWebModel(Person p) {
		if (p == null) {
			id = "";
			firstName = "";
			surName = "";
			lastName = "";
			firstName = "";
			civilId = "";
			birthCity = "";
			civilIdType = "";
			birthDate = "дд.мм.гггг";
		} else {
			id = p.getId() + "";
			firstName = p.getFName();
			surName = p.getSName();
			lastName = p.getLName();
			fullName = p.getFullName();
			civilId = p.getCivilId();
			birthCity = p.getBirthCity();
			civilIdType = p.getCivilIdTypeId() == null ? "" : p.getCivilIdTypeId() + "";
			birthDate = DataConverter.formatDate(p.getBirthDate(), "дд.мм.гггг");

		}

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

}
