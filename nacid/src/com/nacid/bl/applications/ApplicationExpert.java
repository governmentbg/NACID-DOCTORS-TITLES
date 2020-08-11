package com.nacid.bl.applications;

import com.nacid.bl.comision.ComissionMember;
import com.nacid.bl.nomenclatures.ExpertPosition;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.LegalReason;
import com.nacid.bl.nomenclatures.Speciality;

import java.util.List;

public interface ApplicationExpert {
    public int getId();

    public int getExpertId();
    public ComissionMember getExpert();
    public String getNotes();
    public int getProcessStat();


    public String getCourseContent();
    public Integer getExpertPositionId();
    public ExpertPosition getExpertPosition();
    public Integer getEduLevelId();

    public Integer getQualificationId();
    public String getPreviousBoardDecisions();
    public String getSimilarBulgarianPrograms();
    public FlatNomenclature getQualification();
    public FlatNomenclature getEducationLevel();

    public List<Integer> getSpecialityIds();
    public List<Speciality> getSpecialities();

    public Integer getLegalReasonId();
    public LegalReason getLegalReason();
}
