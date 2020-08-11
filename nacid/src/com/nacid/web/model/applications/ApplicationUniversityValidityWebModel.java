package com.nacid.web.model.applications;

import com.nacid.web.model.table.TableWebModel;
/**
 * izpolzva se za generirane na Проверка на висше училище по седалище v taba status na zaqvlenieto
 * @author ggeorgiev
 *
 */
public class ApplicationUniversityValidityWebModel {
	private TableWebModel table;
	private UniversityExaminationWebModel universityExamination;
	private UniversityWebModel university;
	
	public ApplicationUniversityValidityWebModel(TableWebModel table, UniversityExaminationWebModel universityExamination,
			UniversityWebModel university) {
		this.table = table;
		this.universityExamination = universityExamination;
		this.university = university;
	}
	public TableWebModel getTable() {
		return table;
	}
	public UniversityExaminationWebModel getUniversityExamination() {
		return universityExamination;
	}
	public UniversityWebModel getUniversity() {
		return university;
	}
	
}
