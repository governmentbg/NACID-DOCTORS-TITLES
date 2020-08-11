package com.nacid.bl.impl.nomenclatures;

import java.util.Date;

import com.nacid.bl.impl.Utils;
import com.nacid.bl.nomenclatures.FlatNomenclature;

public abstract class FlatNomenclatureImpl implements FlatNomenclature {
  protected int id;
  protected String name;
  protected Date dateFrom;
  protected Date dateTo;
  
  public FlatNomenclatureImpl(int id, String name, Date dateFrom, Date dateTo) {
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
  public abstract int getNomenclatureType();
  public boolean isActive() {
    return Utils.isRecordActive(dateFrom, dateTo);
  }
  public String toString() {
    final String tab = "\n\t";
    StringBuilder retValue = new StringBuilder();
    retValue.append(super.toString());
    retValue.append("FlatNomenclatureImpl ( ")
        .append(tab).append(" id = ").append(this.id)
        .append(tab).append(" name = ").append(this.name)
        .append(tab).append(" dateFrom = ").append(this.dateFrom)
        .append(tab).append(" dateTo = ").append(this.dateTo)
        .append("\n )");
    return retValue.toString();
  }
  
}
