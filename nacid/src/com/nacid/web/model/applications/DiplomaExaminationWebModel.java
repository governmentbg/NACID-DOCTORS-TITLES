package com.nacid.web.model.applications;

import java.util.Date;

import com.nacid.bl.applications.DiplomaExamination;
import com.nacid.data.DataConverter;

public class DiplomaExaminationWebModel {
    private String id = "0";
    private String examinationDate = DataConverter.formatDate(new Date());
    private String notes = "";
    private boolean recognized = false;
    private String competentInstitutionId;
    private boolean institutionCommunicated = false;
    private boolean universityCommunicated = false;
    private boolean foundInRegister = false;
    private boolean isEmpty = true;
    public DiplomaExaminationWebModel(DiplomaExamination de) {
        if (de != null) {
            this.id = de.getId() + "";
            this.examinationDate = DataConverter.formatDate(de.getExaminationDate(), "дд.мм.гггг");
            this.notes = de.getNotes();
            this.recognized = de.isRecognized();
            this.institutionCommunicated = de.isInstitutionCommunicated();
            this.universityCommunicated = de.isUniversityCommunicated();
            this.competentInstitutionId = de.getCompetentInstitutionId() == null ? null : de.getCompetentInstitutionId() +"";
            this.foundInRegister = de.isFoundInRegister();
            this.isEmpty = false;
        }
    }
    public String getId() {
        return id;
    }
    public String getExaminationDate() {
        return examinationDate;
    }
    public String getNotes() {
        return notes;
    }
    public boolean isRecognized() {
        return recognized;
    }
    public boolean isInstitutionCommunicated() {
        return institutionCommunicated;
    }
    public boolean isUniversityCommunicated() {
        return universityCommunicated;
    }
    public String getCompetentInstitutionId() {
        return competentInstitutionId;
    }
	public boolean isEmpty() {
		return isEmpty;
	}

    public boolean isFoundInRegister() {
        return foundInRegister;
    }
}
