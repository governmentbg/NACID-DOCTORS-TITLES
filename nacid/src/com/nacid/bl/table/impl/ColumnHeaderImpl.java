package com.nacid.bl.table.impl;

import com.nacid.bl.table.ColumnHeader;

public class ColumnHeaderImpl implements ColumnHeader{
  private int columnId;
  private String columnName;
  private int cellsType;
  public ColumnHeaderImpl(int columnId, String columnName, int cellsType) {
    this.columnId = columnId;
    this.columnName = columnName;
    this.cellsType = cellsType;
  }
  public void setCellsType(int cellsType) {
    this.cellsType = cellsType;
  }
  public int getColumnCellsType() {
    return cellsType;
  }
  public String getColumnName() {
    return columnName;
  }
  public int getColumnId() {
    return columnId;
  }
  @Override
  public String toString() {
    return super.toString();
  }
}
