package com.nacid.bl.table.record;

import com.nacid.bl.table.CellValue;

public class BooleanRecord extends CellValueRecord {
  public BooleanRecord(Boolean value) {
    this.value = value;
  }
  public boolean getBooleanValue() {
    return value == null ? false : (Boolean) value;
  }
  @Override
  public int compareTo(CellValue o) {
    if (!(o instanceof BooleanRecord)) {
      throw new IllegalArgumentException("You cannot compare cellvalues of different type. o.type = " + o.getCellValueType());
    }
    if (value != null && o.getValue() != null) {
      return ((Boolean)getValue()).compareTo((Boolean)o.getValue());
    }
    return 0;
  }
  public int getCellValueType() {
    return CELL_VALUE_TYPE_BOOLEAN;
  }
  
  
}
