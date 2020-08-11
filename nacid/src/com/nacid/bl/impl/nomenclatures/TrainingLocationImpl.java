package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.TrainingLocation;
import com.nacid.data.nomenclatures.TrainingLocationRecord;

public class TrainingLocationImpl extends FlatNomenclatureImpl implements TrainingLocation {

  public TrainingLocationImpl(TrainingLocationRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_TRAINING_LOCATION;
  }
}
