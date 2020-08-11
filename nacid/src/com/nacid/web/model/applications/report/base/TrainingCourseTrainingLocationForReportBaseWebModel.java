package com.nacid.web.model.applications.report.base;

import com.nacid.bl.applications.base.TrainingCourseTrainingLocationBase;
import com.nacid.bl.nomenclatures.Country;

public class TrainingCourseTrainingLocationForReportBaseWebModel {
	private String trainingCountry;
	private String trainingCity;
	public TrainingCourseTrainingLocationForReportBaseWebModel(TrainingCourseTrainingLocationBase trainingLocation) {
		Country country =  trainingLocation.getTrainingLocationCountry();
		this.trainingCountry = country == null ? "" : country.getName();
		this.trainingCity = trainingLocation.getTrainingLocationCity();
	}
	public String getTrainingCountry() {
		return trainingCountry;
	}
	public String getTrainingCity() {
		return trainingCity;
	}
}
