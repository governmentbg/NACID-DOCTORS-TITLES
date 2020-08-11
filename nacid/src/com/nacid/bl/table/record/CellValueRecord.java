package com.nacid.bl.table.record;

import java.util.Date;

import com.nacid.bl.table.CellValue;

public abstract class CellValueRecord extends CellValue {
  protected Object value;
  public Object getValue() {
    return value;
  }
  public long getIntegerValue() {
    return 0;
  }
  public double getFloatingValue() {
    return 0;
  }
  public String getStringValue() {
    return null;
  }
  public Date getDateValue() {
    return null;
  }
  public boolean getBooleanValue() {
    return false;
  }
}
