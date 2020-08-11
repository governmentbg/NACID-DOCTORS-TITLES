package com.nacid.bl.table.record;

import com.nacid.bl.table.CellValue;

public class StringRecord extends CellValueRecord {
  public StringRecord(String value) {
    this.value = value;
  }
  public String getStringValue() {
    return (String) value;
  }
  public long getIntegerValue() {
    if (value == null) {
      return 0;
    }
    try {
      return Long.parseLong((String)value);  
    } catch (NumberFormatException exc) {
      return 0;
    }
    
  }
  public double getFloatingValue() {
    if (value == null) {
      return 0;
    }
    try {
      return Double.parseDouble((String)value);   
    } catch (NumberFormatException exc) {
      return 0;
    }
  }
  public int compareTo(CellValue o) {
    if (!(o instanceof StringRecord)) {
      throw new IllegalArgumentException("You cannot compare cellvalues of different type. o.type = " + o.getCellValueType());
    }
    if(value == null && o.getValue() != null) {
        return -1;
    }
    else if(value != null && o.getValue() == null) {
        return 1;
    }
    else if (value != null && o.getValue() != null) {
      return ((String)getValue()).compareToIgnoreCase((String)o.getValue());
    }
    return 0;
  }
  public int getCellValueType() {
    return CELL_VALUE_TYPE_STRING;
  }
}
