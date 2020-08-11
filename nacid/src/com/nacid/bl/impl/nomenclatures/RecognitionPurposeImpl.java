package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.RecognitionPurpose;
import com.nacid.data.nomenclatures.RecognitionPurposeRecord;

public class RecognitionPurposeImpl extends FlatNomenclatureImpl implements RecognitionPurpose {

  public RecognitionPurposeImpl(RecognitionPurposeRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_RECOGNITION_PURPOSE;
  }
}
