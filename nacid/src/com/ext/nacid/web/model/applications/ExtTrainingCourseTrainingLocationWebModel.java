package com.ext.nacid.web.model.applications;

import com.nacid.bl.external.applications.ExtTrainingCourseTrainingLocation;

public class ExtTrainingCourseTrainingLocationWebModel {
	private String trainingLocationTrainingCity;
	private Integer trainingLocationTrainingCountryId;
	
	public ExtTrainingCourseTrainingLocationWebModel(String trainingCity, Integer trainingCountryId) {
		this.trainingLocationTrainingCity = trainingCity;
		this.trainingLocationTrainingCountryId = trainingCountryId;
	}
	
	public ExtTrainingCourseTrainingLocationWebModel(ExtTrainingCourseTrainingLocation trainingLocation) {
		this.trainingLocationTrainingCity = trainingLocation.getTrainingLocationCity();
		this.trainingLocationTrainingCountryId = trainingLocation.getTrainingLocationCountryId();
	}
	
	public String getTrainingLocationTrainingCity() {
		return trainingLocationTrainingCity;
	}
	public Integer getTrainingLocationTrainingCountryId() {
		return trainingLocationTrainingCountryId;
	}
	
}
