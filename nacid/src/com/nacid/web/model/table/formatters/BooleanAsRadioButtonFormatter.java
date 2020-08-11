package com.nacid.web.model.table.formatters;

import com.nacid.bl.table.CellValue;
import com.nacid.web.model.table.CellFormatter;
/**
 * za konvertirane na boolean cell values v checknat/nechecknat checkbox
 * @author ggeorgiev
 *
 */
public class BooleanAsRadioButtonFormatter extends CellFormatter {

  public String getValue(CellValue value) {
    return value == null || value.getValue() == null ? "" : "<input type=\"radio\" name=\"_name\" value=\"-\" disabled=\"disabled\" " + (value.getBooleanValue() ? " checked=\"checked\" " : "") + "/>";
  }
  @Override
  public String getCellTitle(CellValue value) {
    return null;
  }
}
