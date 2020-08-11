package com.nacid.web.model.applications;

import com.nacid.web.model.common.ComboBoxWebModel;

public class DiplomaTypeIssuerWebModel {
	private Integer countryId;
	private ComboBoxWebModel universityCombo;
	private ComboBoxWebModel facultyCombo;

	public DiplomaTypeIssuerWebModel(Integer countryId, ComboBoxWebModel universityCombo, ComboBoxWebModel facultyCombo) {
		this.countryId = countryId;
		this.universityCombo = universityCombo;
		this.facultyCombo = facultyCombo;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public ComboBoxWebModel getUniversityCombo() {
		return universityCombo;
	}

	public ComboBoxWebModel getFacultyCombo() {
		return facultyCombo;
	}
}
