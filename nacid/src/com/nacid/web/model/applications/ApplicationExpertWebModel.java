package com.nacid.web.model.applications;

import com.nacid.bl.applications.ApplicationExpert;
import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.DataConverter;
import com.nacid.web.model.common.ComboBoxWebModel;

import java.util.List;

public class ApplicationExpertWebModel {
    private int id;
    private int expertId;
    private String expertName;
    private String notes;
    private int processStat;
    private ComboBoxWebModel combo;
    private String courseContent;
    private String expertPositionId;

    private String qualificationId;
    private List<Speciality> expertSpecialities;
    private String qualificationName;
    private String previousBoardDecisions;
    private String similarBulgarianPrograms;
    private String eduLevelId;
    private String eduLevelName;
    private ComboBoxWebModel legalReasonsCombo;


    public ApplicationExpertWebModel(ApplicationExpert applicationExpert, ComboBoxWebModel combo, ComboBoxWebModel legalReasonsCombo) {
        this.id = applicationExpert.getId();
        this.expertId = applicationExpert.getExpertId();
        ComissionMember comissionMember = applicationExpert.getExpert();
        this.expertName = comissionMember.getTitle() + comissionMember.getFullName();
        this.notes = applicationExpert.getNotes();
        this.processStat = applicationExpert.getProcessStat();


        this.courseContent = applicationExpert.getCourseContent();
        this.expertPositionId = applicationExpert.getExpertPositionId() == null ? "" :applicationExpert.getExpertPositionId().toString();

        this.qualificationId = DataConverter.formatInteger(applicationExpert.getQualificationId());

        FlatNomenclature ell = applicationExpert.getEducationLevel();
        FlatNomenclature qual = applicationExpert.getQualification();

        this.qualificationName = qual == null ? ""  : qual.getName();

        this.previousBoardDecisions = applicationExpert.getPreviousBoardDecisions();
        this.similarBulgarianPrograms = applicationExpert.getSimilarBulgarianPrograms();

        this.eduLevelId = DataConverter.formatInteger(applicationExpert.getEduLevelId());
        this.eduLevelName = ell == null ? "" : ell.getName();
        this.combo = combo;
        expertSpecialities = applicationExpert.getSpecialities();
        this.legalReasonsCombo = legalReasonsCombo;
    }
    public int getId() {
        return id;
    }
    public int getExpertId() {
        return expertId;
    }
    public String getExpertName() {
        return expertName;
    }
    public String getNotes() {
        return notes;
    }
    public int getProcessStat() {
        return processStat;
    }
    public ComboBoxWebModel getCombo() {
        return combo;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public String getExpertPositionId() {
        return expertPositionId;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    public List<Speciality> getExpertSpecialities() {
        return expertSpecialities;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public String getPreviousBoardDecisions() {
        return previousBoardDecisions;
    }

    public String getSimilarBulgarianPrograms() {
        return similarBulgarianPrograms;
    }

    public String getEduLevelId() {
        return eduLevelId;
    }

    public String getEduLevelName() {
        return eduLevelName;
    }

    public ComboBoxWebModel getLegalReasonsCombo() {
        return legalReasonsCombo;
    }
}
