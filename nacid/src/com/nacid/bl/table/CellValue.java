package com.nacid.bl.table;

import java.util.Calendar;
import java.util.Date;

import com.nacid.bl.table.record.BooleanRecord;
import com.nacid.bl.table.record.DateRecord;
import com.nacid.bl.table.record.FloatingRecord;
import com.nacid.bl.table.record.IntegerRecord;
import com.nacid.bl.table.record.StringRecord;

public abstract class CellValue implements CellValueDef, Comparable<CellValue>{

  public static CellValue createCellValue(Object object, int cellValueType) throws CellCreationException  {
    if (cellValueType == CELL_VALUE_TYPE_INTEGER) {
      if (object == null) {
        return new IntegerRecord(null);
      } else if (object instanceof Number) {
        return new IntegerRecord(((Number)object).longValue());
      } else {
        throw new CellCreationException("Unable to create Long(Int) cell value for " + object);
      }
    } else if (cellValueType == CELL_VALUE_TYPE_FLOATING) {
      if (object == null) {
        return new FloatingRecord(null);
      } else if (object instanceof Number) {
        return new FloatingRecord(((Number)object).doubleValue());
      } else {
        throw new CellCreationException("Unable to create float(double) cell value for " + object);
      }
    } else if (cellValueType == CELL_VALUE_TYPE_DATE) {
      if (object == null) {
        return new DateRecord(null);
      } else if (object instanceof Date) {
        return new DateRecord((Date)object);
      } else if (object instanceof Calendar) {
        return new DateRecord(((Calendar)object).getTime());
      } else {
        throw new CellCreationException("Unable to create Date(Calendar) cell value for " + object);
      }
    } else if (cellValueType == CELL_VALUE_TYPE_STRING) {
      if (object == null) {
        return new StringRecord(null);
      } else  {
        return new StringRecord(object.toString());
      }
    } else if (cellValueType == CELL_VALUE_TYPE_BOOLEAN) {
      if (object == null || object instanceof Boolean) {
        return new BooleanRecord((Boolean)object);
      } else {
        throw new CellCreationException("Unable to create Boolean cell value for " + object);
      }
    } else {
      throw new CellCreationException ("cellValueType :" + cellValueType + " is not supported! Object:" + object );
    }
  }
  /**
   * tozi method se polzva za syzdavane na cell value ot string - ideqta mu e da syzdae cell value ot submitnata forma ot user-a
   * no moje bi shte trqbva da pretyrpi promeni - da se zadava tipa na String-a, zashtoto primerno datata moje da e v 1000 razlichni formata
   * (dd-mm-yyyy), (hh:nn:ss dd.mm.yyyy) i t.n.
   * @param cellValueType
   * @param string
   * @return
   * @throws CellCreationException
   */
  /*public static CellValue createCellValueFromString(int cellValueType, String string) throws CellCreationException {
    if (cellValueType == CELL_VALUE_TYPE_DATE) {
      throw new CellCreationException("Creation of date cell value from String is not supported yet");
    } else if (cellValueType == CELL_VALUE_TYPE_STRING) {
      return new StringRecord(string);
    } else if (cellValueType == CELL_VALUE_TYPE_FLOATING) {
      try {
        return new FloatingRecord(Double.parseDouble(string));
      } catch (NumberFormatException e) {
        throw new CellCreationException("Cannot convert double value from " + string);
      }
    } else if (cellValueType == CELL_VALUE_TYPE_INTEGER) {
      try {
        return new IntegerRecord(Long.parseLong(string));
      } catch (NumberFormatException e) {
        throw new CellCreationException("Cannot convert long value from " + string);
      } 
    } else if (cellValueType == CELL_VALUE_TYPE_BOOLEAN) {
      return new BooleanRecord(DataConverter.parseBoolean(string));
    } else {
      throw new CellCreationException("Type " + cellValueType + " not supported!");
    }
  }*/

  public abstract long getIntegerValue();
  public abstract double getFloatingValue();
  public abstract String getStringValue();
  public abstract Date getDateValue();
  public abstract boolean getBooleanValue();
  public abstract Object getValue();
}
