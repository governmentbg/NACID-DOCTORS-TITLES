package com.nacid.web.model.applications;

import com.nacid.bl.applications.UniversityExamination;

public class UniversityExaminationWebModel {
	private String notes;
	public UniversityExaminationWebModel(UniversityExamination ex) {
		this.notes = ex.getNotes();
	}
	public String getNotes() {
		return notes;
	}
}
