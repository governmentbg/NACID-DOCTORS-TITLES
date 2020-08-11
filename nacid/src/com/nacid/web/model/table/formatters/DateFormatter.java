package com.nacid.web.model.table.formatters;

import com.nacid.bl.table.CellValue;
import com.nacid.data.DataConverter;
import com.nacid.web.model.table.CellFormatter;

public class DateFormatter extends CellFormatter {

  public String getValue(CellValue value) {
    return value == null || value.getValue() == null ? "" : DataConverter.formatDate(value.getDateValue());
  }
}
