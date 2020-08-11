package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.CivilIdType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.CivilIdTypeRecord;

public class CivilIdTypeImpl extends FlatNomenclatureImpl implements CivilIdType {

  public CivilIdTypeImpl(CivilIdTypeRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }

  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_CIVIL_ID_TYPE;
  }

}
