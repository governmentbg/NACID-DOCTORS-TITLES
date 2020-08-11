package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.CommissionPosition;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.CommissionPositionRecord;

public class CommissionPositionImpl extends FlatNomenclatureImpl implements CommissionPosition {

  public CommissionPositionImpl(CommissionPositionRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }

  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_COMMISSION_POSITION;
  }

}
