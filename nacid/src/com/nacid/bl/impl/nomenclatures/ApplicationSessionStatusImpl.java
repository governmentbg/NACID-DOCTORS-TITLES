package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.ApplicationSessionStatus;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.ApplicationSessionStatusRecord;

public class ApplicationSessionStatusImpl extends FlatNomenclatureImpl implements ApplicationSessionStatus {

  public ApplicationSessionStatusImpl(ApplicationSessionStatusRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_APPLICATION_SESSION_STATUS;
  }
}
