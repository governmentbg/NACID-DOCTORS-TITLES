package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.EducationLevel;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.EducationLevelRecord;

public class EducationLevelImpl extends FlatNomenclatureImpl implements EducationLevel {

  public EducationLevelImpl(EducationLevelRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_LEVEL;
  }
}
