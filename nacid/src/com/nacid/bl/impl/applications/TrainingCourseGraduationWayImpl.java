package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingCourseGraduationWay;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.TrainingCourseGraduationWayRecord;

public class TrainingCourseGraduationWayImpl implements TrainingCourseGraduationWay {
	private TrainingCourseGraduationWayRecord record;
	private NacidDataProvider nacidDataProvider;
	public TrainingCourseGraduationWayImpl(TrainingCourseGraduationWayRecord record, NacidDataProvider nacidDataProvider) {
		this.record = record;
		this.nacidDataProvider = nacidDataProvider;
	}
	public int getId() {
		return record.getId();
	}
	
	public int getTrainingCourseId() {
		return record.getTrainingCourseId();
	}

	public Integer getGraduationWayId() {
		return record.getGraduationWayId();
	}
	public String getNotes() {
		return record.getNotes();
	}
	public String getGraduationWayName() {
		if (getGraduationWayId() == null) {
			return getNotes();
		}
		FlatNomenclature nom = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_WAY, getGraduationWayId());
		return nom == null ? null : nom.getName();
	}
	
}
