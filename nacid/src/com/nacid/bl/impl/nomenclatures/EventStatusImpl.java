package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.DocCategory;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.EventStatusRecord;

public class EventStatusImpl extends FlatNomenclatureImpl implements DocCategory {

  public EventStatusImpl(EventStatusRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_EVENT_STATUS;
  }
}
