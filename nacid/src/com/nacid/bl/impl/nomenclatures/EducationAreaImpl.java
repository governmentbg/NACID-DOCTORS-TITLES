package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.EducationArea;
import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.data.nomenclatures.EducationAreaRecord;

public class EducationAreaImpl extends FlatNomenclatureImpl implements EducationArea {

  public EducationAreaImpl(EducationAreaRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.FLAT_NOMENCLATURE_EDUCATION_AREA;
  }
}
