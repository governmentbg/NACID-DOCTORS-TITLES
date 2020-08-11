package com.nacid.data.nomenclatures;

import java.sql.Date;

public class SpecialityRecord extends FlatNomenclatureRecord {
  private Integer professionGroupId;
  private String professionGroupName;
  public SpecialityRecord(){
  }
  public SpecialityRecord(int id, String name, Integer professionGroupId, Date dateFrom, Date dateTo, String professionGroupName) {
    super(id, name, dateFrom, dateTo);
    this.professionGroupId = professionGroupId;
    this.professionGroupName = professionGroupName;
  }

  public Integer getProfessionGroupId() {
    return professionGroupId;
  }

  public String getProfessionGroupName() {
    return professionGroupName;
  }
  
  public void setProfessionGroupId(Integer professionGroupId) {
    this.professionGroupId = professionGroupId;
  }

  public void setProfessionGroupName(String professionGroupName) {
    this.professionGroupName = professionGroupName;
  }
  public String toString() {
    final String tab = "\n\t";
    StringBuilder retValue = new StringBuilder();
    retValue.append(super.toString());
    retValue.append("SpecialityRecord ( ")
        .append(tab).append(" professionGroupId = ").append(this.professionGroupId)
        .append(tab).append(" professionGroupName = ").append(this.professionGroupName)
        .append("\n )");
    return retValue.toString();
  }
  
}
