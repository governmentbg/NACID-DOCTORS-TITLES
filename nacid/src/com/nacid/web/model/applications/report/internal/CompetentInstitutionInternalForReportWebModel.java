package com.nacid.web.model.applications.report.internal;

import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.nomenclatures.Country;

public class CompetentInstitutionInternalForReportWebModel {
	private String country;
	private String name;
	public CompetentInstitutionInternalForReportWebModel(CompetentInstitution institution) {
		Country c = institution.getCountry();
		this.country = c == null ? "" : c.getName();
		this.name = institution.getName() + (institution.isActive() ? "" : " (inactive)");
	}
	public String getCountry() {
		return country;
	}
	public String getName() {
		return name;
	}

}
