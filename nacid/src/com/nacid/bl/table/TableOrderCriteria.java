package com.nacid.bl.table;


public class TableOrderCriteria {

  private String columnName;
  private boolean ascending;

  public TableOrderCriteria() {
    //this.columnName = columnName;
    //this.ascending = ascending;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }

  public String getColumnName() {
    return columnName;
  }

  public boolean isAscending() {
    return ascending;
  }
}
