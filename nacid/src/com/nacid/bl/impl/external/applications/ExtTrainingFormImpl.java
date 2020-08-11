package com.nacid.bl.impl.external.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.applications.ExtTrainingForm;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.external.applications.ExtTrainingFormRecord;

public class ExtTrainingFormImpl implements ExtTrainingForm {

    private int id;
    private int trainingCourseId;
    private Integer trainingFormId;
    private String notes;
    private NacidDataProvider nacidDataProvider;
    
    
    public ExtTrainingFormImpl(ExtTrainingFormRecord rec, NacidDataProvider nacidDataProvider) {
        this.id = rec.getId();
        this.trainingCourseId = rec.getTrainingCourseId();
        this.trainingFormId = rec.getTrainingFormId();
        this.notes = rec.getNotes();
        this.nacidDataProvider = nacidDataProvider;
    }

    @Override
    public Integer getTrainingFormId() {
        return trainingFormId;
    }
    public FlatNomenclature getTrainingForm() {
    	return getTrainingFormId() == null ? null : nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM, getTrainingFormId());
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
    public String getTrainingFormName() {
		if (getTrainingFormId() == null) {
			return getNotes();
		}
		FlatNomenclature tf = getTrainingForm();
		return tf == null ? null :  getTrainingForm().getName();
	}
}
