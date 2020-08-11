package com.nacid.data.applications;

public class DiplomaTypeIssuerRecord {
	private int id;
	private int diplomaTypeId;
	private int universityId;
	private int ordNum;
	private Integer facultyId;
	public DiplomaTypeIssuerRecord() {
	}
	public DiplomaTypeIssuerRecord(int id, int diplomaTypeId, int universityId, int ordNum, Integer facultyId) {
		this.id = id;
		this.diplomaTypeId = diplomaTypeId;
		this.universityId = universityId;
		this.ordNum = ordNum;
		this.facultyId = facultyId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDiplomaTypeId() {
		return diplomaTypeId;
	}
	public void setDiplomaTypeId(int diplomaTypeId) {
		this.diplomaTypeId = diplomaTypeId;
	}
	public int getUniversityId() {
		return universityId;
	}
	public void setUniversityId(int universityId) {
		this.universityId = universityId;
	}
	public int getOrdNum() {
		return ordNum;
	}
	public void setOrdNum(int ordNum) {
		this.ordNum = ordNum;
	}

	public Integer getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(Integer facultyId) {
		this.facultyId = facultyId;
	}
}
