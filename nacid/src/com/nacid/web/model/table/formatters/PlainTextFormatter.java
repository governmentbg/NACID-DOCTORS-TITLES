package com.nacid.web.model.table.formatters;

import com.nacid.bl.table.CellValue;
import com.nacid.web.model.table.CellFormatter;

public class PlainTextFormatter extends CellFormatter {
  public String getValue(CellValue value) {
    return value == null || value.getValue() == null ? "" : value.getStringValue();
  }

}
