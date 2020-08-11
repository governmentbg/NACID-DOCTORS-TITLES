package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.SchoolType;
import com.nacid.data.nomenclatures.SchoolTypeRecord;

public class SchoolTypeImpl extends FlatNomenclatureImpl implements SchoolType {

  public SchoolTypeImpl(SchoolTypeRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_SCHOOL_TYPE;
  }
}
