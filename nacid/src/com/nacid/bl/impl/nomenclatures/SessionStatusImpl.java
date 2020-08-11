package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.SessionStatus;
import com.nacid.data.nomenclatures.SessionStatusRecord;

public class SessionStatusImpl extends FlatNomenclatureImpl implements SessionStatus {

  public SessionStatusImpl(SessionStatusRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_SESSION_STATUS;
  }
}
