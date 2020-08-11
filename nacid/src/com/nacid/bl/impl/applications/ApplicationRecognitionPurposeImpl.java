package com.nacid.bl.impl.applications;

import com.nacid.bl.NacidDataProvider;
import com.nacid.bl.applications.ApplicationRecognitionPurpose;
import com.nacid.bl.nomenclatures.FlatNomenclature;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.applications.ApplicationRecognitionPurposeRecord;

public class ApplicationRecognitionPurposeImpl implements ApplicationRecognitionPurpose {
	ApplicationRecognitionPurposeRecord record;
	NacidDataProvider nacidDataProvider;
	public ApplicationRecognitionPurposeImpl(ApplicationRecognitionPurposeRecord record, NacidDataProvider nacidDataProvider) {
		this.record = record;
		this.nacidDataProvider = nacidDataProvider;
	}
	public int getId() {
		return record.getId();
	}
	public int getApplicationId() {
		return record.getApplicationId();
	}
	public Integer getRecognitionPurposeId() {
		return record.getRecognitionPurposeId();
	}
	public String getNotes() {
		return record.getNotes();
	}
	public String getRecognitionPurposeNotes() {
		if (getRecognitionPurposeId() == null) {
			return getNotes();
		}
		FlatNomenclature fn = nacidDataProvider.getNomenclaturesDataProvider().getFlatNomenclature(NomenclaturesDataProvider.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE, getRecognitionPurposeId());
		return fn == null ? null : fn.getName();
	}
	

}
