package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.DurationUnit;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.DurationUnitRecord;

public class DurationUnitImpl extends FlatNomenclatureImpl implements DurationUnit {

  public DurationUnitImpl(DurationUnitRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_DURATION_UNIT;
  }
}
