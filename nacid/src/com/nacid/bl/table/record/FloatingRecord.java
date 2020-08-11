package com.nacid.bl.table.record;

import com.nacid.bl.table.CellValue;


public class FloatingRecord extends CellValueRecord {
  //double value;
  public FloatingRecord(Double value) {
    this.value = value;
  }
  public long getIntegerValue() {
    return value == null ? 0 : ((Double)value).longValue();
  }
  public double getFloatingValue() {
    return value == null ? 0 : (Double) value;
  }
  public String getStringValue() {
    return String.valueOf(value);
  }
  @Override
  public int compareTo(CellValue o) {
    if (!(o instanceof FloatingRecord)) {
      throw new IllegalArgumentException("You cannot compare cellvalues of different type. o.type = " + o.getCellValueType());
    }
    if (value != null && o.getValue() != null) {
      return ((Double)getValue()).compareTo((Double)o.getValue());
    }
    return 0;
  }
  public int getCellValueType() {
    return CELL_VALUE_TYPE_FLOATING;
  }
}
