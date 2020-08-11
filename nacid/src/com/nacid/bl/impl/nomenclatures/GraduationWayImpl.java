package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.GraduationWay;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.GraduationWayRecord;

public class GraduationWayImpl extends FlatNomenclatureImpl implements GraduationWay {

  public GraduationWayImpl(GraduationWayRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADUATION_WAY;
  }
}
