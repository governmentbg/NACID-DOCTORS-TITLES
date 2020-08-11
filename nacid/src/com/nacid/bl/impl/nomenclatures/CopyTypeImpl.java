package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.CopyType;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.CopyTypeRecord;

public class CopyTypeImpl extends FlatNomenclatureImpl implements CopyType {

  public CopyTypeImpl(CopyTypeRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_COPY_TYPE;
  }
}
