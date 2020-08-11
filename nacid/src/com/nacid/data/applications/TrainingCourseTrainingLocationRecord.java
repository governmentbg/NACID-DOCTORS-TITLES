package com.nacid.data.applications;

public class TrainingCourseTrainingLocationRecord {
	private int id;
	private int trainingCourseId;
	private Integer trainingCountryId;
	private String trainingCity;
	private Integer trainingInstitutionId;
	public TrainingCourseTrainingLocationRecord() {
	}
	public TrainingCourseTrainingLocationRecord(int id, int trainingCourseId, Integer trainingCountryId, String trainingCity, Integer trainingInstitutionId) {
		this.id = id;
		this.trainingCourseId = trainingCourseId;
		this.trainingCountryId = trainingCountryId;
		this.trainingCity = trainingCity;
		this.trainingInstitutionId = trainingInstitutionId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTrainingCourseId() {
		return trainingCourseId;
	}
	public void setTrainingCourseId(int trainingCourseId) {
		this.trainingCourseId = trainingCourseId;
	}
	
	public Integer getTrainingCountryId() {
		return trainingCountryId;
	}
	public void setTrainingCountryId(Integer trainingCountryId) {
		this.trainingCountryId = trainingCountryId;
	}
	public String getTrainingCity() {
		return trainingCity;
	}
	public void setTrainingCity(String trainingCity) {
		this.trainingCity = trainingCity;
	}
	public Integer getTrainingInstitutionId() {
		return trainingInstitutionId;
	}
	public void setTrainingInstitutionId(Integer trainingInstitutionId) {
		this.trainingInstitutionId = trainingInstitutionId;
	}
	
}
