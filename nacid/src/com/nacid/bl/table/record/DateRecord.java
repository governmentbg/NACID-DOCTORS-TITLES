package com.nacid.bl.table.record;

import java.util.Date;

import com.nacid.bl.table.CellValue;

public class DateRecord extends CellValueRecord {
  public DateRecord(Date value) {
    this.value = value;
  }
  public Date getDateValue() {
    return (Date)value;
  }
  @Override
  public int compareTo(CellValue o) {
    if (!(o instanceof DateRecord)) {
      throw new IllegalArgumentException("You cannot compare cellvalues of different type. o.type = " + o.getCellValueType());
    }
    if (value == null) {
      return -1;
    } else if (o.getValue() == null) {
      return 1;
    } else {// if (value != null && o.getValue() != null) {
      return ((Date)getValue()).compareTo((Date)o.getValue());
    }
    //return 0;
  }
  @Override
  public int getCellValueType() {
    return CELL_VALUE_TYPE_DATE;
  }
}
