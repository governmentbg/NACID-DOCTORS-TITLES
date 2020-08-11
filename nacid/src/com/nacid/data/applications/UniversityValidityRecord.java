package com.nacid.data.applications;

import java.sql.Date;

public class UniversityValidityRecord {

	private int id;
	private Integer universityId;
	private Integer userId;
	private Date examinationDate;
	private int isComunicated;
	private int isRecognized;
	private String notes;
	private Integer trainingLocationId;
	private int hasJoinedDegrees;

	public UniversityValidityRecord() {
	    
	}
	
	public UniversityValidityRecord(int id, Integer universityId, Integer userId, Date examinationDate, int isComunicated, int isRecognized,
			String notes, Integer trainingLocationId, int hasJoinedDegrees) {

		this.id = id;
		this.universityId = universityId;
		this.userId = userId;
		this.examinationDate = examinationDate;
		this.isComunicated = isComunicated;
		this.isRecognized = isRecognized;
		this.notes = notes;
		this.trainingLocationId = trainingLocationId;
		this.hasJoinedDegrees = hasJoinedDegrees;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setUniversityId(Integer universityId) {
		this.universityId = universityId;
	}

	public Integer getUniversityId() {
		return universityId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setExaminationDate(Date examinationDate) {
		this.examinationDate = examinationDate;
	}

	public Date getExaminationDate() {
		return examinationDate;
	}

	public void setIsComunicated(int isComunicated) {
		this.isComunicated = isComunicated;
	}

	public int getIsComunicated() {
		return isComunicated;
	}

	public void setIsRecognized(int isRecognized) {
		this.isRecognized = isRecognized;
	}

	public int getIsRecognized() {
		return isRecognized;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

	public void setTrainingLocationId(Integer trainingLocationId) {
		this.trainingLocationId = trainingLocationId;
	}

	public Integer getTrainingLocationId() {
		return trainingLocationId;
	}

	public void setHasJoinedDegrees(int hasJoinedDegrees) {
		this.hasJoinedDegrees = hasJoinedDegrees;
	}

	public int getHasJoinedDegrees() {
		return hasJoinedDegrees;
	}
}
