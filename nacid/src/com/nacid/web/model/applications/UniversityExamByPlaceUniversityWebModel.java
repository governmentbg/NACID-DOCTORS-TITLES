package com.nacid.web.model.applications;

public class UniversityExamByPlaceUniversityWebModel {
	private UniversityWebModel university;
	private boolean hasJointDegrees;
	private String trainingLocation;
	public UniversityExamByPlaceUniversityWebModel(UniversityWebModel university, boolean hasJointDegrees, String trainingLocation) {
		this.university = university;
		this.hasJointDegrees = hasJointDegrees;
		this.trainingLocation = trainingLocation;
	}
	public UniversityWebModel getUniversity() {
		return university;
	}
	public boolean isHasJointDegrees() {
		return hasJointDegrees;
	}
	public String getTrainingLocation() {
		return trainingLocation;
	}
	
}
