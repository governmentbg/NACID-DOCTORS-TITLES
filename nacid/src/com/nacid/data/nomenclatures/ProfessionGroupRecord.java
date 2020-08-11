package com.nacid.data.nomenclatures;

import java.sql.Date;

public class ProfessionGroupRecord extends FlatNomenclatureRecord {
  private int educationAreaId;
  private String educationAreaName;
  public ProfessionGroupRecord(){
  }
  public ProfessionGroupRecord(int id, String name, int educationAreaId, Date dateFrom, Date dateTo, String educationAreaName) {
    super(id, name, dateFrom, dateTo);
    this.educationAreaId = educationAreaId;
    this.educationAreaName = educationAreaName;
  }

  public int getEducationAreaId() {
    return educationAreaId;
  }

  public String getEducationAreaName() {
    return educationAreaName;
  }
  public void setEducationAreaId(int educationAreaId) {
    this.educationAreaId = educationAreaId;
  }

}
