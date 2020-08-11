package com.ext.nacid.web.model.applications;

public class ExtUniversityWithFacultyWebModel {
	private String universityId;
	private String universityName;
	private String facultyId;
	private String facultyName;

	public ExtUniversityWithFacultyWebModel(String universityId, String universityName, String facultyId, String facultyName) {
		this.universityId = universityId;
		this.universityName = universityName;
		this.facultyId = facultyId;
		this.facultyName = facultyName;
	}

	public String getUniversityId() {
		return universityId;
	}

	public String getUniversityName() {
		return universityName;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public String getFacultyName() {
		return facultyName;
	}
}
