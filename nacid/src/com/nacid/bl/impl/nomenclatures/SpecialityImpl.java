package com.nacid.bl.impl.nomenclatures;

import com.nacid.bl.nomenclatures.NomenclaturesDataProvider;
import com.nacid.bl.nomenclatures.Speciality;
import com.nacid.data.nomenclatures.SpecialityRecord;

public class SpecialityImpl extends FlatNomenclatureImpl implements Speciality {
  private Integer professionGroupId;
  private String professionGroupName;
  public SpecialityImpl(SpecialityRecord record) {
    super(record.getId(), record.getName(), record.getDateFrom(), record.getDateTo());
    this.professionGroupId = record.getProfessionGroupId();
    this.professionGroupName = record.getProfessionGroupName();
  }

  public Integer getProfessionGroupId() {
    return professionGroupId;
  }
  public String getProfessionGroupName() {
    return professionGroupName;
  }
  
  public int getNomenclatureType() {
    return NomenclaturesDataProvider.NOMENCLATURE_SPECIALITY;
  }

}
