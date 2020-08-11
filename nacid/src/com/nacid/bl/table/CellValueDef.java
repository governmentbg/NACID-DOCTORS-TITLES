package com.nacid.bl.table;

import java.util.Calendar;
import java.util.Date;


/**
 * definira tipovete na kletkata
 * zasega samo cqlo 4islo, plavashta zapetaq date, String
 * CELL_VALUE_TYPE_FLOATING - float/double
 * CELL_VALUE_TYPE_INTEGER - int/long
 * CELL_VALUE_TYPE_DATE - {@link java.util.Date} / {@link Calendar}
 * CELL_VALUE_TYPE_STRING - vseki obekt, na koito se vika object.toString()
 * @author ggeorgiev
 *
 */
public interface CellValueDef {
  /**
   * tipa na kletkata e float ili double 
   */
  public static final int CELL_VALUE_TYPE_FLOATING = 1;
  /**
   * tipa na kletkata e long ili integer
   */
  public static final int CELL_VALUE_TYPE_INTEGER = 2;
  /**
   * tipa na kletkata e {@link Date} ili {@link Calendar}
   */
  public static final int CELL_VALUE_TYPE_DATE = 3;
  
  /**
   * tipa na kletkata e {@link String}
   */
  public static final int CELL_VALUE_TYPE_STRING = 4;
  
  /**
   * tipa na kletkata e {@link Boolean}
   */
  public static final int CELL_VALUE_TYPE_BOOLEAN = 5;
  
  public int getCellValueType();
  
  
}
