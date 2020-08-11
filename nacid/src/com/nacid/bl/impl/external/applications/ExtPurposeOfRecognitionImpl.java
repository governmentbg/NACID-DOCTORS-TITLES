package com.nacid.bl.impl.external.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.external.applications.ExtPurposeOfRecognition;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.external.applications.ExtPurposeOfRecognitionRecord;

public class ExtPurposeOfRecognitionImpl implements ExtPurposeOfRecognition {

    private int id;
    private int applicationId;
    private Integer purposeOfRecognitionId;
    private String notes;
    private NacidDataProvider nacidDataProvider;
   
    
    public ExtPurposeOfRecognitionImpl(ExtPurposeOfRecognitionRecord rec, NacidDataProvider nacidDataProvider) {
        super();
        this.id = rec.getId();
        this.applicationId = rec.getApplicationId();
        this.purposeOfRecognitionId = rec.getPurposeOfRecognitionId();
        this.notes = rec.getNotes();
        this.nacidDataProvider = nacidDataProvider;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getApplicationId() {
        return applicationId;
    }

    @Override
    public Integer getPurposeOfRecognitionId() {
        return purposeOfRecognitionId;
    }

    @Override
    public String getNotes() {
        return notes;
    }
    
    public String getRecognitionPurposeNotes() {
		if (getPurposeOfRecognitionId() == null) {
			return getNotes();
		}
		FlatNomenclature fn = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE, getPurposeOfRecognitionId());
		return fn == null ? null : fn.getName();
	}
    
    
}
