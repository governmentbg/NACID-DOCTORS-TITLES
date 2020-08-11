package com.nacid.web.model.table;

import com.nacid.bl.table.CellValue;
import com.nacid.web.model.table.formatters.BooleanAsCheckBoxFormatter;
import com.nacid.web.model.table.formatters.BooleanAsRadioButtonFormatter;
import com.nacid.web.model.table.formatters.DateFormatter;
import com.nacid.web.model.table.formatters.DateTimeFormatter;
import com.nacid.web.model.table.formatters.FloatingFormatter;
import com.nacid.web.model.table.formatters.PlainTextFormatter;
import com.nacid.web.model.table.formatters.TimeFormatter;

public abstract class CellFormatter {
  public static final int PLAIN_TEXT_FORMATTER = 1;
  public static final int DATE_TIME_FORMATTER = 2;
  public static final int DATE_FORMATTER = 3;
  public static final int TIME_FORMATTER = 4;
  //public static final int INTEGER_FORMATTER = 6;
  public static final int FLOATING_FORMATTER = 5;
  public static final int BOOLEAN_AS_CHECKBOX_FORMATTER = 6;
  public static final int BOOLEAN_AS_RADIO_FORMATTER = 7;
 
  
  /**
   * @param cellFormatterId - edin ot definiranite v {@link CellFormatter}
   * @return
   * @throws IllegalArgumentException - ako se podade tip razli4en ot definiranite v {@link CellFormatter} se hvyrlq exception
   */
  public static CellFormatter createCellFormatterByFormatterId(int cellFormatterId) throws IllegalArgumentException{
    CellFormatter formatter;
    if (cellFormatterId == PLAIN_TEXT_FORMATTER) {
      formatter  = new PlainTextFormatter();
    } else if (cellFormatterId == DATE_TIME_FORMATTER) {
      formatter  = new DateTimeFormatter();
    } else if (cellFormatterId == TIME_FORMATTER) {
      formatter  = new TimeFormatter();
    } else if (cellFormatterId == DATE_FORMATTER) {
      formatter  = new DateFormatter();
    } else if (cellFormatterId == FLOATING_FORMATTER) {
      formatter  = new FloatingFormatter();
    } else if (cellFormatterId == BOOLEAN_AS_CHECKBOX_FORMATTER) {
      formatter  = new BooleanAsCheckBoxFormatter();
    } else if (cellFormatterId == BOOLEAN_AS_RADIO_FORMATTER) {
      formatter  = new BooleanAsRadioButtonFormatter();
    } else {
      throw new IllegalArgumentException("Illegal cell formatter id");
    }
    return formatter;
  }
  /**
   * @param cellValueType - edin ot definiranite v {@link CellValue} tipove
   * @return - podrazbirasht se CellFormatter na baza tipa danni v dadena kletka
   * @throws IllegalArgumentException - ako se podade stojnost razli4na ot definiranite v {@link CellFormatter}
   */
  public static CellFormatter createCellFormatterByCellValueType(int cellValueType) throws IllegalArgumentException {
    if (cellValueType == CellValue.CELL_VALUE_TYPE_DATE) {
      return new DateFormatter();
    } else if (cellValueType == CellValue.CELL_VALUE_TYPE_FLOATING) {
      return new FloatingFormatter();
    } else if (cellValueType == CellValue.CELL_VALUE_TYPE_INTEGER) {
      return new PlainTextFormatter();
    } else if (cellValueType == CellValue.CELL_VALUE_TYPE_STRING) {
      return new PlainTextFormatter();
    } else if (cellValueType == CellValue.CELL_VALUE_TYPE_BOOLEAN) {
      return new BooleanAsCheckBoxFormatter();
    } else {
      throw new IllegalArgumentException("Cannot create cell formatter. Illegal cell value type");
    }
  }
  public abstract String getValue(CellValue value);

  public String getCellTitle(CellValue value) {
    return getValue(value);
  }
}
