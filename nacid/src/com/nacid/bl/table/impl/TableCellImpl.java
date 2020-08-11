package com.nacid.bl.table.impl;

import java.util.Date;

import com.nacid.bl.table.CellCreationException;
import com.nacid.bl.table.CellValue;
import com.nacid.bl.table.TableCell;

public class TableCellImpl implements Comparable<TableCellImpl>, TableCell {
  
  private CellValue cellValue;
  private TableRowImpl row;
  public TableCellImpl(TableRowImpl tableRow, Object object, int cellValueType) throws CellCreationException {
    this.row = tableRow;
    setCellValue(object, cellValueType);
  }
  /*public void setFloatingValue(double value) {
    this.cellValue = new FloatingRecord(value);
  }
  public void setIntegerValue(long value) {
    this.cellValue = new IntegerRecord(value);
  }
  public void setDateValue(Date date) {
    this.cellValue = new DateRecord(date);
  }
  public void setStringValue(String value) {
    this.cellValue = new StringRecord(value);
  }*/
  private void setCellValue(Object object, int cellValueType) throws CellCreationException {
    this.cellValue = CellValue.createCellValue(object, cellValueType);
  }
  public long getIntegerValue() {
    return cellValue.getIntegerValue();
  }
  public double getFloatingValue() {
    return cellValue.getFloatingValue();
  }
  public Date getDateValue() {
    return cellValue.getDateValue();
  }
  public String getStringValue() {
    return cellValue.getStringValue();
  }
  
  public int getCellValueType() {
    return cellValue.getCellValueType();
  }
  public CellValue getCellValue() {
    return cellValue;
  }
  
  public int compareTo(TableCellImpl cell) {
    if (cell.getCellValueType() != getCellValueType()) {
      return 0;
    }
    if (cell == null || cell.getCellValue() == null || getCellValue() == null) {
      return 0;
    }
    
    
    if (cell != null && getCellValue() != null) {
      return getCellValue().compareTo(cell.getCellValue());
    }
    return 0;
  }
  public TableRowImpl getRow() {
    return row;
  }
  /*public int getRowNumber() {
    return rowNumber;
  }
  public int getColNumber() {
    return colNumber;
  }*/
}
