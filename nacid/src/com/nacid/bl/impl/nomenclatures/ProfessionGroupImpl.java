package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.ProfessionGroup;
import com.nacid.data.nomenclatures.ProfessionGroupRecord;

public class ProfessionGroupImpl extends FlatNomenclatureImpl implements ProfessionGroup {
  private int educationAreaId;
  private String educationAreaName;
  public ProfessionGroupImpl(ProfessionGroupRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    this.educationAreaId = record.getEducationAreaId();
    this.educationAreaName = record.getEducationAreaName();
  }
  public int getEducationAreaId() {
    return educationAreaId;
  }
  public String getEducationAreaName() {
    return educationAreaName;
  }
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.NOMENCLATURE_PROFESSION_GROUP;
  }

}
