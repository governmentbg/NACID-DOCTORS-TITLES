package com.nacid.bl.table.record;

import com.nacid.bl.table.CellValue;

public class IntegerRecord extends CellValueRecord {
  public IntegerRecord(Long value) {
    this.value = value;
  }
  public long getIntegerValue() {
    return value == null ? 0 : (Long) value;
  }
  public double getFloatingValue() {
    return value == null ? 0 : ((Long) value).doubleValue();
  }
  public String getStringValue() {
    return value + "";
  }
  @Override
  public int compareTo(CellValue o) {
    if (!(o instanceof IntegerRecord)) {
      throw new IllegalArgumentException("You cannot compare cellvalues of different type. o.type = " + o.getCellValueType());
    }
    if (value != null && o.getValue() != null) {
      return ((Long)getValue()).compareTo((Long)o.getValue());
    }
    return 0;
  }
  public int getCellValueType() {
    return CELL_VALUE_TYPE_INTEGER;
  }
  
  
}
