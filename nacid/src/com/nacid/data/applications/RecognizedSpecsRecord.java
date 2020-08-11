package com.nacid.data.applications;

public class RecognizedSpecsRecord {
	private int id;
	private int trainingCourseId;
	private int specId;
	
	public RecognizedSpecsRecord() {
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
	public int getSpecId() {
		return specId;
	}
	public void setSpecId(int specId) {
		this.specId = specId;
	}
	
}
