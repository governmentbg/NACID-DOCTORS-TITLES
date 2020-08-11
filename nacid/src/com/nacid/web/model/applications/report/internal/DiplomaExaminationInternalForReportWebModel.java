package com.nacid.web.model.applications.report.internal;

import com.nacid.bl.applications.CompetentInstitution;
import com.nacid.bl.applications.DiplomaExamination;
import com.nacid.bl.nomenclatures.Country;
import com.nacid.data.DataConverter;

public class DiplomaExaminationInternalForReportWebModel {
	private String examinationDate;
	private String competentInstitution;
	private String competentInstitutionUrl;
	private boolean institutionCommunicated;
	private boolean universityCommunicated;
	private boolean recognized;
	private boolean foundInRegister;
	private String notes;
	public DiplomaExaminationInternalForReportWebModel(DiplomaExamination de) {
		this.examinationDate = DataConverter.formatDate(de.getExaminationDate(), "");
		CompetentInstitution competentInstitution = de.getCompetentInstitution();
		Country c = competentInstitution == null ? null : competentInstitution.getCountry();
		this.competentInstitution = competentInstitution == null ? "" : competentInstitution.getName() + (c == null ? "" : " / " + c.getName());
		this.competentInstitutionUrl = competentInstitution == null ? null : competentInstitution.getUrl();
		this.notes = de.getNotes();
		this.institutionCommunicated = de.isInstitutionCommunicated();
		this.universityCommunicated = de.isUniversityCommunicated();
		this.recognized = de.isRecognized();
		this.foundInRegister = de.isFoundInRegister();
	}
	public String getExaminationDate() {
		return examinationDate;
	}
	public String getCompetentInstitution() {
		return competentInstitution;
	}
	public String getCompetentInstitutionUrl() {
		return competentInstitutionUrl;
	}
	public boolean isInstitutionCommunicated() {
		return institutionCommunicated;
	}
	public boolean isUniversityCommunicated() {
		return universityCommunicated;
	}
	public boolean isRecognized() {
		return recognized;
	}
	public String getNotes() {
		return notes;
	}

	public boolean isFoundInRegister() {
		return foundInRegister;
	}
}
