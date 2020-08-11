package com.ext.nacid.web.model.applications.report;

import com.nacid.bl.external.applications.ExtUniversity;
import com.nacid.web.model.applications.UniversityWebModel;

public class ExtUniversityForReportWebModel extends UniversityWebModel {
	private String universityTxt;
	public ExtUniversityForReportWebModel(ExtUniversity university) {
		super(university);
		this.universityTxt = university.getUniversityTxt();
	}
	public String getUniversityTxt() {
		return universityTxt;
	}

}
