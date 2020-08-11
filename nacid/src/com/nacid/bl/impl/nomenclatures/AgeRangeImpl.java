package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.AgeRange;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.AgeRangeRecord;

public class AgeRangeImpl extends FlatNomenclatureImpl implements AgeRange {

  public AgeRangeImpl(AgeRangeRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_AGE_RANGE;
  }
}
