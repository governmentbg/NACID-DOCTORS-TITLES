package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.TrainingCourseTrainingForm;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.TrainingCourseTrainingFormRecord;

public class TrainingCourseTrainingFormImpl implements TrainingCourseTrainingForm {
	private TrainingCourseTrainingFormRecord record;
	private NacidDataProvider nacidDataProvider;
	public TrainingCourseTrainingFormImpl(NacidDataProvider nacidDataProvider, TrainingCourseTrainingFormRecord record) {
		this.record = record;
		this.nacidDataProvider = nacidDataProvider;
	}
	public int getId() {
		return record.getId();
	}
	public int getTrainingCourseId() {
		return record.getTrainingCourseId();
	}
	public Integer getTrainingFormId() {
		return record.getTrainingFormId();
	}
	public FlatNomenclature getTrainingForm() {
		return getTrainingFormId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM, getTrainingFormId());
	}
	public String getNotes() {
		return record.getNotes();
	}
	public String getTrainingFormName() {
		if (getTrainingFormId() == null) {
			return getNotes();
		}
		FlatNomenclature tf = getTrainingForm();
		return tf == null ? null :  getTrainingForm().getName();
	}
	
}
