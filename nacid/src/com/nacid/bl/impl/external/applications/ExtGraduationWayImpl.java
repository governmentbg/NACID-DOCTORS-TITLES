package com.nacid.bl.impl.external.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.applications.ExtGraduationWay;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.external.applications.ExtGraduationWayRecord;

public class ExtGraduationWayImpl implements ExtGraduationWay {

    private int id;
    private int trainingCourseId;
    private Integer graduationWayId;
    private String notes;
    private NacidDataProvider nacidDataProvider;
    
    
    public ExtGraduationWayImpl(ExtGraduationWayRecord rec, NacidDataProvider nacidDataProvider) {
        super();
        this.id = rec.getId();
        this.trainingCourseId = rec.getTrainingCourseId();
        this.graduationWayId = rec.getGraduationWayId();
        this.notes = rec.getNotes();
        this.nacidDataProvider = nacidDataProvider;
    }

    @Override
    public Integer getGraduationWayId() {
        return graduationWayId;
    }

    @Override
    public int getTrainingCourseId() {
        return trainingCourseId;
    }

    @Override
    public int getId() {
       return id;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    public String getGraduationWayName() {
		if (getGraduationWayId() == null) {
			return getNotes();
		}
		FlatNomenclature nom = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_WAY, getGraduationWayId());
		return nom == null ? null : nom.getName();
	}
}
