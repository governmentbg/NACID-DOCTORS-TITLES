package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.BgAcademicRecognitionStatus;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.BgAcademicRecognitionStatusRecord;

public class BgAcademicRecognitionStatusImpl extends FlatNomenclatureImpl implements BgAcademicRecognitionStatus{

	public BgAcademicRecognitionStatusImpl(BgAcademicRecognitionStatusRecord record) {
		super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
	}

	@Override
	public int getNomenclatureType() {
		return NomenclaturesDataProvider.FLAT_NOMENCLATURE_BG_ACAD_REC_STATUS;
	}

}
