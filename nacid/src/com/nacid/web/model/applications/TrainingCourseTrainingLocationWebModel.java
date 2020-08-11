package com.nacid.web.model.applications;

import com.nacid.bl.applications.TrainingCourseTrainingLocation;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.web.taglib.FormInputUtils;

public class TrainingCourseTrainingLocationWebModel {
	private int id;
	private String trainingLocationTrainingCity;
	private Integer trainingLocationTrainingCountryId;
	private String trainingLocationTrainingCountry;
	private String trainingInstitutionId;
	private String hasTrainingInstitution;
	public TrainingCourseTrainingLocationWebModel(String trainingCity, Integer trainingCountryId) {
		this.trainingLocationTrainingCity = trainingCity;
		this.trainingLocationTrainingCountryId = trainingCountryId;
	}
	public TrainingCourseTrainingLocationWebModel(TrainingCourseTrainingLocation trainingLocation) {
		this(trainingLocation, false);
	}
	public TrainingCourseTrainingLocationWebModel(TrainingCourseTrainingLocation trainingLocation, boolean generateTrainingCountryName) {
		this.id = trainingLocation.getId();
		this.trainingLocationTrainingCity = trainingLocation.getTrainingLocationCity();
		this.trainingLocationTrainingCountryId = trainingLocation.getTrainingLocationCountryId();
		this.trainingInstitutionId = trainingLocation.getTrainingInstitutionId() == null ? null : trainingLocation.getTrainingInstitutionId() + "";
		this.hasTrainingInstitution = FormInputUtils.getCheckBoxCheckedText(this.trainingInstitutionId != null);
		if (generateTrainingCountryName) {
			Country c = trainingLocation.getTrainingLocationCountry();
			trainingLocationTrainingCountry = c == null ? "" : c.getName();
		}
		
	}
	public String getTrainingLocationTrainingCity() {
		return trainingLocationTrainingCity;
	}
	public Integer getTrainingLocationTrainingCountryId() {
		return trainingLocationTrainingCountryId;
	}
	public String getTrainingLocationTrainingCountry() {
		return trainingLocationTrainingCountry;
	}
	public int getId() {
		return id;
	}
	public String getTrainingInstitutionId() {
		return trainingInstitutionId;
	}
	public String getHasTrainingInstitution() {
		return hasTrainingInstitution;
	}
	
}
