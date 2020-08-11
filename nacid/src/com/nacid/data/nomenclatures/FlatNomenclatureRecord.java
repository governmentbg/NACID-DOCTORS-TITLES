package com.nacid.data.nomenclatures;

import java.sql.Date;

public abstract class FlatNomenclatureRecord {
  protected int id;
  protected String name;
  protected Date dateFrom;
  protected Date dateTo;
  
  public FlatNomenclatureRecord() {
  }
  public FlatNomenclatureRecord(int id, String name, Date dateFrom, Date dateTo) {
    this.id = id;
    this.name = name;
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
  }
  public int getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public Date getDateFrom() {
    return dateFrom;
  }
  public Date getDateTo() {
    return dateTo;
  }
  public void setId(int id) {
    this.id = id;
  }
  public void setName(String name) {
    this.name = name;
  }
  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }
  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }
  public String toString() {
    final String tab = "\n\t";
    StringBuilder retValue = new StringBuilder();
    retValue.append(super.toString());
    retValue.append("FlatNomenclatureRecord ( ")
        .append(tab).append(" id = ").append(this.id)
        .append(tab).append(" name = ").append(this.name)
        .append(tab).append(" dateFrom = ").append(this.dateFrom)
        .append(tab).append(" dateTo = ").append(this.dateTo)
        .append("\n )");
    return retValue.toString();
  }
  
}
