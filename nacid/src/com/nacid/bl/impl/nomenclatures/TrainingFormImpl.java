package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.TrainingForm;
import com.nacid.data.nomenclatures.TrainingFormRecord;

public class TrainingFormImpl extends FlatNomenclatureImpl implements TrainingForm {

  public TrainingFormImpl(TrainingFormRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_FORM;
  }
}
