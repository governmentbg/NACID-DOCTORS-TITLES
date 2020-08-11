package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.Grade;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.GradeRecord;

public class GradeImpl extends FlatNomenclatureImpl implements Grade {

  public GradeImpl(GradeRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_GRADE;
  }
}
