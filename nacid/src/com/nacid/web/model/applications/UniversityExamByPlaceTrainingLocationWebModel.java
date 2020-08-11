package com.nacid.web.model.applications;


public class UniversityExamByPlaceTrainingLocationWebModel {
	private TrainingCourseTrainingLocationWebModel trainingLocation;
	//private ComboBoxWebModel institutionsCombo;
	private String trainingInstitutionId;
	
	public UniversityExamByPlaceTrainingLocationWebModel(TrainingCourseTrainingLocationWebModel trainingLocation, String trainingInstitutionId) {
		this.trainingLocation = trainingLocation;
		this.trainingInstitutionId = trainingInstitutionId;
	}
	public TrainingCourseTrainingLocationWebModel getTrainingLocation() {
		return trainingLocation;
	}
	public String getTrainingInstitutionId() {
		return trainingInstitutionId;
	}
	
	
}
